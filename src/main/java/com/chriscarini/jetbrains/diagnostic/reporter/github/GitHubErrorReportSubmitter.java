package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.chriscarini.jetbrains.diagnostic.reporter.PluginUtils;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;


/**
 * <h2>Error report submitter for GitHub.</h2>
 * <p>
 * This error report submitter will intelligently choose between an <b>authenticated submitter</b> and an <b>anonymous
 * submitter</b>.
 * </p><p>
 * If the GitHub plugin is installed, enabled, and the user has selected a GitHub account, the <b>authenticated
 * submitter</b> will be used. Otherwise, the <b>anonymous submitter</b> will be used.
 * </p>
 * <h3>Authenticated Submitter</h3>
 * <p>
 * The <b>authenticated submitter</b> will use the user's GitHub account to automatically open a GitHub issue against
 * the specified GitHub project.
 * </p>
 * <h3>Anonymous Submitter</h3>
 * <p>
 * The <b>anonymous submitter</b> will open a GitHub link that goes to a pre-filled GitHub issue form. The user will
 * need to manually submit the issue.
 * </p>
 */
public class GitHubErrorReportSubmitter extends ErrorReportSubmitter {
    private static final Logger LOG = Logger.getInstance(GitHubErrorReportSubmitter.class);

    private BaseGitHubErrorReportSubmitter authdSubmitter;
    private BaseGitHubErrorReportSubmitter anonySubmitter;
    private final String gitHubRepoUrl;
    private final List<String> assignees;
    private final List<String> labels;
    private final boolean openReportInBrowser;

    /**
     * Default constructor.
     *
     * @param gitHubRepoUrl A GitHub repository URL to submit the issue to.
     * @param assignees     A list of assignees to assign the issue to. Can be null.
     * @param labels        A list of labels to apply to the issue. Can be null.
     */
    public GitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final java.util.List<String> assignees,
            @Nullable final List<String> labels
    ) {
        this(gitHubRepoUrl, assignees, labels, true);
    }

    private GitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            boolean openReportInBrowser
    ) {
        super();
        this.gitHubRepoUrl = gitHubRepoUrl;

        this.assignees = assignees;
        this.labels = labels;
        this.openReportInBrowser = openReportInBrowser;
        initGithubErrorReportSubmitters();
    }

    private void initGithubErrorReportSubmitters() {
        if (anonySubmitter == null) {
            anonySubmitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, assignees, labels, openReportInBrowser);
        }
        if (authdSubmitter == null && isGitHubPluginInstalledAndEnabled()) {
            try {
                authdSubmitter = new AuthenticatedGitHubErrorReportSubmitter(gitHubRepoUrl, assignees, labels, openReportInBrowser);
            } catch (NoClassDefFoundError e) {
                LOG.warn("Error creating AuthenticatedGitHubErrorReportSubmitter", e);
                authdSubmitter = null;
            }
        }
    }

    private BaseGitHubErrorReportSubmitter getSubmitter() {
        initGithubErrorReportSubmitters();
        if (isGitHubPluginInstalledAndEnabled() && authdSubmitter != null) {
            LOG.info("GitHub plugin is installed. Using authenticated submitter.");
            return authdSubmitter;
        } else {
            LOG.info("GitHub plugin is not installed. Using anonymous submitter.");
            return anonySubmitter;
        }
    }

    private static boolean isGitHubPluginInstalledAndEnabled() {
        final String gitHubPluginId = "org.jetbrains.plugins.github";

        boolean isPluginInstalled = PluginUtils.isPluginInstalled(gitHubPluginId);
        LOG.info("GitHub plugin installed: " + isPluginInstalled);

        boolean isPluginEnabled = PluginUtils.isPluginEnabled(gitHubPluginId);
        LOG.info("GitHub plugin enabled: " + isPluginEnabled);

        return isPluginInstalled && isPluginEnabled;
    }

    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return getSubmitter().getReportActionText();
    }

    @Override
    public @Nullable String getReporterAccount() {
        return getSubmitter().getReporterAccount();
    }

    @Override
    public void changeReporterAccount(@NotNull Component parentComponent) {
        getSubmitter().changeReporterAccount(parentComponent);
    }

    @Override
    public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo, @NotNull Component
            parentComponent, @NotNull Consumer<? super SubmittedReportInfo> consumer) {
        return getSubmitter().submit(events, additionalInfo, parentComponent, consumer);
    }
}
