package com.chriscarini.jetbrains.diagnostic.reporter.multi;

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
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Error report submitter for Multiple {@link ErrorReportSubmitter}'s
 */
public class MultiErrorReportSubmitter extends ErrorReportSubmitter {
    private static final Logger LOG = Logger.getInstance(MultiErrorReportSubmitter.class);

    private final List<? extends ErrorReportSubmitter> submitters;
    private final boolean anySuccessful;
    @NotNull @NlsActions.ActionText protected final String actionText;

    /**
     * Default constructor.
     *
     * @param submitters    The list of {@link ErrorReportSubmitter}'s to submit to
     * @param anySuccessful If true, any successful {@link ErrorReportSubmitter}'s submission will constitute a
     *                      successful error report submission. If false, all submissions must be successful to
     *                      constitute a successful error report submission.
     * @param actionText    The text to display for the report action button.
     */
    public MultiErrorReportSubmitter(
            final List<? extends ErrorReportSubmitter> submitters,
            final boolean anySuccessful,
            @NotNull @NlsActions.ActionText final String actionText
    ) {
        super();
        this.submitters = submitters;
        this.anySuccessful = anySuccessful;
        this.actionText = actionText;
    }

    @Override
    public boolean submit(
            IdeaLoggingEvent @NotNull [] events,
            @Nullable String additionalInfo,
            @NotNull Component parentComponent,
            @NotNull Consumer<? super SubmittedReportInfo> consumer
    ) {
        final Stream<Boolean> stream = submitters.stream().parallel().map(submitter -> {
            final String className = submitter.getClass().getName();
            LOG.debug(String.format("Submitted to %s", className));
            boolean submitted = submitter.submit(events, additionalInfo, parentComponent, consumer);
            LOG.debug(String.format("Submitted to %s: %s", className, submitted));
            return submitted;
        });

        final Boolean result = this.anySuccessful
                ? stream.reduce(Boolean::logicalOr).orElse(false)
                : stream.reduce(Boolean::logicalAnd).orElse(true);

        LOG.debug(String.format("Result is %s", result));
        return result;
    }

    @Override
    public @Nullable String getReporterAccount() {
        return submitters.stream().map(ErrorReportSubmitter::getReporterAccount)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void changeReporterAccount(@NotNull Component parentComponent) {
        submitters.forEach(submitter -> {
            try {
                submitter.changeReporterAccount(parentComponent);
            } catch (UnsupportedOperationException e) {
                LOG.warn("Error changing reporter account", e);
            }
        });
    }

    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return this.actionText;
    }
}