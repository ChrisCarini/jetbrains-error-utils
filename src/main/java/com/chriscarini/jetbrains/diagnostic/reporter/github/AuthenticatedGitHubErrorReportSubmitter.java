package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.util.Consumer;
import git4idea.DialogManager;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.github.api.GHRepositoryPath;
import org.jetbrains.plugins.github.api.GithubApiRequestExecutor;
import org.jetbrains.plugins.github.api.GithubApiRequests;
import org.jetbrains.plugins.github.api.GithubServerPath;
import org.jetbrains.plugins.github.authentication.GHAccountsUtil;
import org.jetbrains.plugins.github.authentication.accounts.GithubAccount;
import org.jetbrains.plugins.github.authentication.ui.GithubChooseAccountDialog;
import org.jetbrains.plugins.github.i18n.GithubBundle;
import org.jetbrains.plugins.github.util.GHCompatibilityUtil;
import org.jetbrains.plugins.github.util.GithubNotifications;
import org.jetbrains.plugins.github.util.GithubUrlUtil;


/**
 * An Authenticated {@link BaseGitHubErrorReportSubmitter}.
 * <p>
 * Authenticated, which allows us to:
 * <ol>
 * <li>automatically open the issue on the users behalf (no need for the user to click "Create Issue" in browser)</li>
 * <li>provide more details in the issue</li>
 * <li>have more explicitness that an issue was successfully submitted or not</li>
 * </ol>
 */
class AuthenticatedGitHubErrorReportSubmitter extends BaseGitHubErrorReportSubmitter {
    private static final Logger LOG = Logger.getInstance(AuthenticatedGitHubErrorReportSubmitter.class);

    private GithubAccount reporterGithubAccount;

    @SuppressWarnings("unused")
    protected AuthenticatedGitHubErrorReportSubmitter(
            @NotNull String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels
    ) {
        super(gitHubRepoUrl, assignees, labels);
    }

    protected AuthenticatedGitHubErrorReportSubmitter(
            @NotNull String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            boolean openReportInBrowser
    ) {
        super(gitHubRepoUrl, assignees, labels, openReportInBrowser);
    }

    @Override
    public @Nullable String getReporterAccount() {
        if (this.reporterGithubAccount == null) {
            final Project project = getLastFocusedOrOpenedProject();
            this.reporterGithubAccount = GitHubUtils.getReporterGithubAccount(project);
        }

        return this.reporterGithubAccount != null ? String.format("%s/%s", this.reporterGithubAccount.getServer(), this.reporterGithubAccount.getName()) : null;
    }

    @Override
    public void changeReporterAccount(@NotNull Component parentComponent) {
        final Project project = getLastFocusedOrOpenedProject();

        Set<GithubAccount> accounts = GHAccountsUtil.getAccounts();

        GithubChooseAccountDialog chooseAccountDialog =
                new GithubChooseAccountDialog(project, parentComponent, accounts, "Which GitHub account would you like to use to submit the error report?", true,
                        false);
        DialogManager.show(chooseAccountDialog);
        if (chooseAccountDialog.isOK()) {
            final GithubAccount githubAccount = chooseAccountDialog.getAccount();
            if (chooseAccountDialog.getSetDefault()) {
                GHAccountsUtil.setDefaultAccount(project, githubAccount);
            }
            this.reporterGithubAccount = githubAccount;
        } else {
            GithubNotifications.showError(project, "FAILED_TO_SELECT_VALID_GITHUB_ACCOUNT", "Failed to select GitHub account",
                    "Failed to select GitHub account. Try again.");
        }
    }

    @Override
    public boolean submit(
            IdeaLoggingEvent @NotNull [] events,
            @Nullable String additionalInfo,
            @NotNull Component parentComponent,
            @NotNull Consumer<? super SubmittedReportInfo> consumer
    ) {
        final String title = super.getTitle(events, additionalInfo, 150);
        final String body = super.getBody(events, additionalInfo, Integer.MAX_VALUE);

        final Project project = getLastFocusedOrOpenedProject();
        final GithubAccount githubAccount = this.reporterGithubAccount;
        final String repoUrl = this.gitHubRepoUrl;

        final Ref<String> url = new Ref<>();

        new Task.Backgroundable(project, "Submitting issue to GitHub...") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                final String token = GHCompatibilityUtil.getOrRequestToken(githubAccount, project);
                if (token == null) {
                    return;
                }
                final GithubApiRequestExecutor requestExecutor = GithubApiRequestExecutor.Factory.getInstance().create(token);

                final GithubServerPath server = new GithubServerPath(GithubServerPath.from(repoUrl).getHost());
                final GHRepositoryPath repositoryPath = GithubUrlUtil.getUserAndRepositoryFromRemoteUrl(repoUrl);
                if (repositoryPath == null) {
                    LOG.warn("Failed to get repository path from URL: " + repoUrl);
                    url.set(null);
                    return;
                }
                final String username = repositoryPath.getOwner();
                final String repoName = repositoryPath.getRepository();
                final Long milestone = null;
                final List<String> labels = getLabels();
                final List<String> assignees = getAssignees();

                try {
                    final String issueUrl = requestExecutor.execute(
                            indicator,
                            GithubApiRequests.Repos.Issues.create(
                                    server,
                                    username,
                                    repoName,
                                    title,
                                    body,
                                    milestone,
                                    labels,
                                    assignees
                            )
                    ).getHtmlUrl();
                    url.set(issueUrl);
                } catch (IOException e) {
                    url.set(null);
                }
            }

            @Override
            public void onSuccess() {
                if (url.isNull()) {
                    consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED));
                    return;
                }
                
                openInBrowser(url.get());

                GithubNotifications.showInfoURL(project, "jb.error.utils.github.notification", "GitHub issue created successfully",
                        GithubBundle.message("create.gist.url"), url.get());
                consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
            }

            @Override
            public void onThrowable(@NotNull Throwable error) {
                super.onThrowable(error);
                consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED));
            }
        }.queue();

        return true;
    }
}