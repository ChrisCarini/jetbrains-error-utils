package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.Consumer;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;


public class AnonymousGitHubErrorReportSubmitterTest2 extends BasePlatformTestCase {
    public void testSubmit() throws IOException {
        // given
        final AnonymousGitHubErrorReportSubmitter submitter = new AnonymousGitHubErrorReportSubmitter("https://TEST_URL", null, null, false);

        IdeaLoggingEvent[] events = new IdeaLoggingEvent[1];
        Throwable mockThrowable = Mockito.mock(Throwable.class);
        events[0] = new IdeaLoggingEvent(null, mockThrowable);

        Component mockComponent = Mockito.mock(Component.class);
        final boolean[] consumed = {false};
        Consumer<? super SubmittedReportInfo> dummyConsumer = (Consumer<SubmittedReportInfo>) submittedReportInfo -> consumed[0] = true;

        // when
        boolean result = submitter.submit(events, null, mockComponent, dummyConsumer);

        // then
        assert result;
        assert consumed[0];
    }
}
