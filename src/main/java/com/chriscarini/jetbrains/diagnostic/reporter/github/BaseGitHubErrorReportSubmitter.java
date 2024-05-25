package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.chriscarini.jetbrains.diagnostic.reporter.Messages;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.troubleshooting.CompositeGeneralTroubleInfoCollector;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.IdeFrame;
import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A base GitHub {@link ErrorReportSubmitter}.
 * <p>
 * Intended to be subclassed by other GitHub {@link ErrorReportSubmitter}'s.
 */
class BaseGitHubErrorReportSubmitter extends ErrorReportSubmitter {
    @NonNls
    private static final String TRIMMED_STACKTRACE_MARKER = "\n\n<TRIMMED STACKTRACE>";

    static final Consumer<String> OPEN_IN_BROWSER_CONSUMER = BrowserUtil::browse;

    protected final String gitHubRepoUrl;

    @Nullable
    private final List<String> assignees;
    @Nullable
    private final List<String> labels;

    @NonNls
    protected final boolean openReportInBrowser;

    @NotNull
    private final Consumer<String> openInBrowserConsumer;

    public BaseGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels
    ) {
        this(gitHubRepoUrl, assignees, labels, true);
    }

    public BaseGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            final boolean openReportInBrowser
    ) {
        this(gitHubRepoUrl, assignees, labels, openReportInBrowser, OPEN_IN_BROWSER_CONSUMER);
    }


    public BaseGitHubErrorReportSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            final boolean openReportInBrowser,
            @NotNull final Consumer<String> openInBrowserConsumer
    ) {
        super();
        this.gitHubRepoUrl = gitHubRepoUrl;
        this.assignees = assignees;
        this.labels = labels;
        this.openReportInBrowser = openReportInBrowser;
        this.openInBrowserConsumer = openInBrowserConsumer;
    }

    @Nullable
    public List<String> getAssignees() {
        return assignees;
    }

    @Nullable
    public List<String> getLabels() {
        return labels;
    }

    @NotNull
    protected String getTitle(
            @NotNull IdeaLoggingEvent[] events,
            @SuppressWarnings("unused") @Nullable String additionalInfo,
            int maxLength
    ) {
        final IdeaLoggingEvent event = getEvent(events);

        // contains the simple error message - "Error message" field
        final String simpleErrorMessage = event != null ? event.getMessage() : Messages.message("jetbrains.error.utils.github.error.report.submitter.no.events");

        return StringUtils.abbreviate(generateIssueSummary(simpleErrorMessage, getStackTrace(event)), maxLength);
    }

    @NotNull
    protected String getBody(
            @NotNull IdeaLoggingEvent[] events,
            @Nullable String additionalInfo,
            int maxLength
    ) {
        // contains information entered by user - "Please fill in any details that may be important..." field
        final String userProvidedInformation = additionalInfo != null ? additionalInfo : Messages.message("jetbrains.error.utils.github.error.report.submitter.user.did.not.enter.any.detailed.information");

        return generateIssueDescription(userProvidedInformation, getStackTrace(getEvent(events)), maxLength);
    }

    private static String getStackTrace(IdeaLoggingEvent event) {
        return event != null ? event.getThrowableText() : Messages.message("jetbrains.error.utils.github.error.report.submitter.no.stacktrace");
    }

    private static @Nullable IdeaLoggingEvent getEvent(@NotNull IdeaLoggingEvent[] events) {
        return events.length > 0 ? events[0] : null;
    }

    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return "\uD83D\uDC1B " + Messages.message(
                "jetbrains.error.utils.github.error.report.submitter.report.action.text",
                GitHubUtils.extractUsername(this.gitHubRepoUrl)
        );
    }

    @NonNls
    private static final String GITHUB_ISSUE_SUMMARY_FORMAT = "[\uD83D\uDC1B bug][%s %s] %s";

    @NotNull
    private String generateIssueSummary(@Nullable final String simpleErrorMessage, @NotNull final String stackTrace) {
        final String errorMessage = simpleErrorMessage == null || simpleErrorMessage.isEmpty() ? stackTrace.split("\n\t")[0] : simpleErrorMessage;

        final ApplicationInfoEx appInfo = ApplicationInfoEx.getInstanceEx();
        final ApplicationNamesInfo applicationNamesInfo = ApplicationNamesInfo.getInstance();
        return String.format(GITHUB_ISSUE_SUMMARY_FORMAT, applicationNamesInfo.getProductName(), appInfo.getFullVersion(), errorMessage);
    }

    private static String wrapGitHubCode(final String code) {
        return String.format("```\n%s\n```", code);
    }

    @Language("Markdown")
    @NonNls
    private static final String MAIN_GITHUB_DESCRIPTION_FORMAT =
            String.join("\n", List.of(
                    "# User Description\n%s\n",
                    "# Stack Trace\n" + wrapGitHubCode("%s") + "\n",
                    "# Other Information\n%s\n"
            ));


    @NotNull
    private String generateIssueDescription(@NotNull final String userDescription, @NotNull final String stackTrace, int maxLength) {
        final String defaultHelpBlock = getDefaultHelpBlock();
        final String abbreviatedStackTrace = StringUtils.abbreviate(
                stackTrace,
                TRIMMED_STACKTRACE_MARKER,
                maxLength - defaultHelpBlock.length() - userDescription.length()
        );
        return String.format(MAIN_GITHUB_DESCRIPTION_FORMAT, userDescription, abbreviatedStackTrace, defaultHelpBlock);
    }

    /**
     * Generate the default help information.
     *
     * @return String formatted for GitHub
     */
    private String getDefaultHelpBlock() {
        final String generalTroubleshootingInformation = new CompositeGeneralTroubleInfoCollector().collectInfo(getLastFocusedOrOpenedProject());

        final String trimmedGeneralTroubleshootingInformation = (
                Stream.of(generalTroubleshootingInformation.split("\n"))
                        .takeWhile((@NonNls String s) -> !"=== System ===".equals(s)) // Take everything before the "System" section; we want it.
                        .filter((@NonNls String s) -> !s.startsWith("idea."))         // Filter out the `idea.<>.path` lines, as we don't want them
                        .collect(Collectors.joining("\n"))
        ) + "\n" + (
                Stream.of(generalTroubleshootingInformation.split("\n"))
                        .dropWhile((@NonNls String s) -> !"=== Plugins ===".equals(s)) // Skip everything before the "Plugins" section; we grabbed that in the stream above.
                        .takeWhile((@NonNls String s) -> !"=== Project ===".equals(s)) // Skip the "Project" section; we don't want that.
                        .collect(Collectors.joining("\n"))
        );

        return wrapGitHubCode(trimmedGeneralTroubleshootingInformation.trim());
    }

    void openInBrowser(@NotNull final String url) {
        if (this.openReportInBrowser) {
            this.openInBrowserConsumer.accept(url);
        }
    }

    /**
     * Get the last focused or opened project; this is a best-effort attempt.
     * Code pulled from {org.jetbrains.ide.RestService}
     *
     * @return Project that was last in focus or open.
     */
    @NotNull
    protected static Project getLastFocusedOrOpenedProject() {
        final IdeFrame lastFocusedFrame = IdeFocusManager.getGlobalInstance().getLastFocusedFrame();
        final Project project = lastFocusedFrame == null ? null : lastFocusedFrame.getProject();
        if (project == null) {
            final ProjectManager projectManager = ProjectManager.getInstance();
            final Project[] openProjects = projectManager.getOpenProjects();
            return openProjects.length > 0 ? openProjects[0] : projectManager.getDefaultProject();
        }
        return project;
    }
}
