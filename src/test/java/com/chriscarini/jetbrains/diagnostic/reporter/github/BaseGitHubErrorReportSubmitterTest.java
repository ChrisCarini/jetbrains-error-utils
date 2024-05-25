package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.chriscarini.jetbrains.diagnostic.reporter.Messages;
import com.intellij.ide.troubleshooting.CompositeGeneralTroubleInfoCollector;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class BaseGitHubErrorReportSubmitterTest extends BasePlatformTestCase {
    public void testCompositeGeneralTroubleInfoCollectorDOTcollectInfo() {
        // when
        final String result = new CompositeGeneralTroubleInfoCollector().collectInfo(
                BaseGitHubErrorReportSubmitter.getLastFocusedOrOpenedProject()
        );

        // then
        assert !result.isEmpty();
    }

    public void testGetAssigneesNull() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };

        // when
        final List<String> result = baseGitHubErrorReportSubmitter.getAssignees();

        // then 
        assert result == null;
    }

    public void testGetAssignees() {
        // given  
        final String assignee1 = "assignee1";
        final String assignee2 = "assignee2";
        final List<String> assignees = List.of(assignee1, assignee2);
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", assignees, null) {
        };

        // when
        final List<String> result = baseGitHubErrorReportSubmitter.getAssignees();

        // then 
        assert assignees.equals(result);
    }

    public void testGetLabels() {
        // given  
        final String label1 = "label1";
        final String label2 = "label2";
        final List<String> labels = List.of(label1, label2);
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, labels) {
        };

        // when
        final List<String> result = baseGitHubErrorReportSubmitter.getLabels();

        // then 
        assert labels.equals(result);
    }

    public void testGetLabels_Null() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };

        // when
        final List<String> result = baseGitHubErrorReportSubmitter.getLabels();

        // then 
        assert result == null;
    }

    private void assertTitle(@NotNull final String title, @NotNull final String expectedTitle) {
        final ApplicationInfoEx appInfo = ApplicationInfoEx.getInstanceEx();
        final ApplicationNamesInfo applicationNamesInfo = ApplicationNamesInfo.getInstance();
        assert title.contains(applicationNamesInfo.getProductName());
        assert title.contains(appInfo.getFullVersion());
        assert title.contains(expectedTitle);
    }

    public void testGetTitle() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };
        final String event1Message = "Event1";
        final String event2Message = "Event2";
        final String exception1 = "Exception1";
        final String exception2 = "Exception2";
        final IdeaLoggingEvent[] events = Arrays.asList(
                new IdeaLoggingEvent(event1Message, new Throwable(exception1)),
                new IdeaLoggingEvent(event2Message, new Throwable(exception2))
        ).toArray(new IdeaLoggingEvent[0]);
        final String additionalInfo = "additionalInfo";

        // when
        final String result = baseGitHubErrorReportSubmitter.getTitle(events, additionalInfo, 100);

        // then
        assertTitle(result, event1Message);
        assert !result.contains(event2Message);
        assert !result.contains(exception1);
        assert !result.contains(exception2);
        assert !result.contains(additionalInfo);
    }

    public void testGetTitle_EmptyEventMessage() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };
        final String event1Message = "";
        final String event2Message = "Event2";
        final String exception1 = "Exception1";
        final String exception2 = "Exception2";
        final IdeaLoggingEvent[] events = Arrays.asList(
                new IdeaLoggingEvent(event1Message, new Throwable(exception1)),
                new IdeaLoggingEvent(event2Message, new Throwable(exception2))
        ).toArray(new IdeaLoggingEvent[0]);
        final String additionalInfo = "additionalInfo";

        // when
        final String result = baseGitHubErrorReportSubmitter.getTitle(events, additionalInfo, 100);

        // then 
        assertTitle(result, exception1);
        assert !result.contains(event2Message);
        assert !result.contains(exception2);
        assert !result.contains(additionalInfo);
    }

    public void testGetTitle_NoEvents() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };
        final IdeaLoggingEvent[] events = new IdeaLoggingEvent[0];
        final String additionalInfo = "additionalInfo";

        // when
        final String result = baseGitHubErrorReportSubmitter.getTitle(events, additionalInfo, 100);

        // then 
        assertTitle(result, Messages.message("jetbrains.error.utils.github.error.report.submitter.no.events"));
        assert !result.contains(additionalInfo);
    }

    private void assertBody(@NotNull final String body, @NotNull final String expectedBody) {
        final ApplicationInfoEx appInfo = ApplicationInfoEx.getInstanceEx();
        final ApplicationNamesInfo applicationNamesInfo = ApplicationNamesInfo.getInstance();
        assert body.contains(applicationNamesInfo.getProductName());
        assert body.contains(appInfo.getFullVersion());
        assert body.contains(expectedBody);
    }

    public void testGetBody() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };
        final String event1Message = "Event1";
        final String event2Message = "Event2";
        final String exception1 = "Exception1";
        final String exception2 = "Exception2";
        final IdeaLoggingEvent[] events = Arrays.asList(
                new IdeaLoggingEvent(event1Message, new Throwable(exception1)),
                new IdeaLoggingEvent(event2Message, new Throwable(exception2))
        ).toArray(new IdeaLoggingEvent[0]);
        final String additionalInfo = "additionalInfo";

        // when
        final String result = baseGitHubErrorReportSubmitter.getBody(events, additionalInfo, 1000);

        // then
        assertBody(result, exception1);
        assert result.contains("# User Description");
        assert result.contains(additionalInfo);
        assert result.contains("# Other Information");
        assert result.contains("=== About ===");
        assert result.contains("=== Plugins ===");
        assert !result.contains(event1Message);
        assert !result.contains(event2Message);
        assert !result.contains(exception2);
    }

    public void testGetBody_NoEventsNoAdditionalInfo() {
        // given  
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter("gitHubRepoUrl", null, null) {
        };
        final IdeaLoggingEvent[] events = new IdeaLoggingEvent[0];

        // when
        final String result = baseGitHubErrorReportSubmitter.getBody(events, null, 1000);

        // then
        assertBody(result, "```\n" + Messages.message("jetbrains.error.utils.github.error.report.submitter.no.stacktrace") + "\n```");
        assert result.contains("# User Description");
        assert result.contains(Messages.message("jetbrains.error.utils.github.error.report.submitter.user.did.not.enter.any.detailed.information"));
        assert result.contains("# Other Information");
        assert result.contains("=== About ===");
        assert result.contains("=== Plugins ===");
    }

    public void testGetReportActionText() {
        // given  
        final String githubUsername = "githubUsername";
        final String githubRepoUrl = String.format("https://github.com/%s/MY_COOL_REPO/", githubUsername);
        final BaseGitHubErrorReportSubmitter baseGitHubErrorReportSubmitter = new BaseGitHubErrorReportSubmitter(githubRepoUrl, null, null) {
        };

        // when
        final String result = baseGitHubErrorReportSubmitter.getReportActionText();

        // then
        assert result.contains(githubUsername);
    }
}
