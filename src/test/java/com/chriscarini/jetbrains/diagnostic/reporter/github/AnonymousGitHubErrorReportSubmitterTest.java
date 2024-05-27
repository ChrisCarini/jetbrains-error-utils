package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.Consumer;

import java.awt.Component;
import java.util.List;


public class AnonymousGitHubErrorReportSubmitterTest extends BasePlatformTestCase {

    public static final Component PARENT_COMPONENT = new Component() {
        @Override
        public String getName() {
            return "foo";
        }
    };

    public void testSubmit() {
        // given  
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter("https://TEST_URL", null, null, false);

        // when 
        boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }

    public void testSubmit_OpenBrowser() {
        // given  
        final String gitHubRepoUrl = "https://TEST_URL";
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final java.util.function.Consumer<String> openInBrowser = url -> {
            // then
            assert url.contains(gitHubRepoUrl);
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, null, null, true, openInBrowser);

        // when
        final boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }

    public void testSubmit_WithAssignees() {
        // given  
        final String assignee1 = "assignee1";
        final String assignee2 = "assignee2";
        final List<String> assignees = List.of(assignee1, assignee2);
        final String gitHubRepoUrl = "https://TEST_URL";
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final java.util.function.Consumer<String> openInBrowser = url -> {
            // then
            assert url.contains(gitHubRepoUrl);
            assert url.contains("assignees=");
            assert url.contains(assignee1);
            assert url.contains(assignee2);
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, assignees, null, true, openInBrowser);

        // when 
        boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }

    public void testSubmit_WithAssignees_Empty() {
        // given  
        final List<String> assignees = List.of();
        final String gitHubRepoUrl = "https://TEST_URL";
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final java.util.function.Consumer<String> openInBrowser = url -> {
            // then
            assert url.contains(gitHubRepoUrl);
            assert !url.contains("assignees=");
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, assignees, null, true, openInBrowser);

        // when 
        boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }

    public void testSubmit_WithLabels() {
        // given  
        final String label1 = "label1";
        final String label2 = "label2";
        final List<String> labels = List.of(label1, label2);
        final String gitHubRepoUrl = "https://TEST_URL";
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final java.util.function.Consumer<String> openInBrowser = url -> {
            // then
            assert url.contains(gitHubRepoUrl);
            assert url.contains("labels=");
            assert url.contains(label1);
            assert url.contains(label2);
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, null, labels, true, openInBrowser);

        // when 
        boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }

    public void testSubmit_WithLabels_Empty() {
        // given  
        final List<String> labels = List.of();
        final String gitHubRepoUrl = "https://TEST_URL";
        final Consumer<? super SubmittedReportInfo> dummyConsumer = submittedReportInfo -> {
            // then
            assert submittedReportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
        };
        final java.util.function.Consumer<String> openInBrowser = url -> {
            // then
            assert url.contains(gitHubRepoUrl);
            assert !url.contains("labels=");
        };
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter(gitHubRepoUrl, null, labels, true, openInBrowser);

        // when 
        boolean result = submitter.submit(new IdeaLoggingEvent[0], null, PARENT_COMPONENT, dummyConsumer);

        // then 
        assertTrue(result);
    }
}