package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * An unauthenticated {@link BaseGitHubErrorReportSubmitter}.
 * <p>
 * Unauthenticated, allowing easier implementation for the plugin developer.
 */
class AnonymousGitHubErrorReportSubmitter extends BaseGitHubErrorReportSubmitter {


    @SuppressWarnings("unused")
    protected AnonymousGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels
    ) {
        super(gitHubRepoUrl, assignees, labels);
    }

    protected AnonymousGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            final boolean openReportInBrowser
    ) {
        super(gitHubRepoUrl, assignees, labels, openReportInBrowser);
    }


    protected AnonymousGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            final boolean openReportInBrowser,
            @NotNull final java.util.function.Consumer<String> openInBrowserConsumer
    ) {
        super(gitHubRepoUrl, assignees, labels, openReportInBrowser, openInBrowserConsumer);
    }

    @Override
    public boolean submit(
            IdeaLoggingEvent @NotNull [] events,
            @Nullable String additionalInfo,
            @NotNull Component parentComponent,
            @NotNull Consumer<? super SubmittedReportInfo> consumer
    ) {
        final String title = super.getTitle(events, additionalInfo, 120);
        final String body = super.getBody(events, additionalInfo, 6500);

        openInBrowser(buildUrl(title, body));

        consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
        return true;
    }

    @NotNull
    private @NonNls String buildUrl(@NotNull final String title, @NotNull final String body) {
        final List<String> params = new ArrayList<>();

        final List<String> assignees = getAssignees();
        if (assignees != null && !assignees.isEmpty()) {
            params.add(String.format("assignees=%s", String.join(",", assignees)));  //   10 chars + list of GH usernames
        }

        final List<String> labels = getLabels();
        if (labels != null && !labels.isEmpty()) {
            params.add(String.format("labels=%s", String.join(",", labels)));        //   7 chars + list of labels
        }
        params.add("title=" + URLEncoder.encode(title, StandardCharsets.UTF_8));             //   6 chars + encoded title (max 120 chars)
        params.add("body=" + URLEncoder.encode(body, StandardCharsets.UTF_8));               //   5 chars + encoded body (max 6500 chars)

        return String.join(
                "",
                this.gitHubRepoUrl,                                                // unknown chars
                "/issues/new?",                                                              //  12 chars
                String.join("&", params)
        );
    }
}
