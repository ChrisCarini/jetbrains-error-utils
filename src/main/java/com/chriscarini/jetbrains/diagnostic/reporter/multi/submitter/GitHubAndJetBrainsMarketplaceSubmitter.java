package com.chriscarini.jetbrains.diagnostic.reporter.multi.submitter;


import com.chriscarini.jetbrains.diagnostic.reporter.PluginUtils;
import com.chriscarini.jetbrains.diagnostic.reporter.github.GitHubErrorReportSubmitter;
import com.chriscarini.jetbrains.diagnostic.reporter.multi.MultiErrorReportSubmitter;
import com.intellij.diagnostic.JetBrainsMarketplaceErrorReportSubmitter;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A {@link MultiErrorReportSubmitter} for both GitHub and JetBrains Marketplace.
 */
public class GitHubAndJetBrainsMarketplaceSubmitter extends MultiErrorReportSubmitter {

    /**
     * Constructor for a {@link GitHubAndJetBrainsMarketplaceSubmitter}.
     *
     * @param pluginId                The plugin ID.
     * @param productionGitHubRepoUrl The GitHub repository URL to use in the published plugin.
     * @param sandboxGitHubRepoUrl    The GitHub repository URL to use when running in development mode (i.e. the
     *                                sandbox).
     * @param assignees               A list of assignees to add to the GitHub issue. Only works for authenticated users
     *                                that are maintainers of the GitHub repository.
     * @param labels                  A list of labels to add to the GitHub issue. Only works for authenticated users
     *                                that are maintainers of the GitHub repository.
     * @param actionText              The text to display for the report action button.
     */
    @SuppressWarnings("unused")
    public GitHubAndJetBrainsMarketplaceSubmitter(
            @NotNull final String pluginId,
            @NotNull final String productionGitHubRepoUrl,
            @NotNull final String sandboxGitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            @NotNull @NlsActions.ActionText final String actionText
    ) {
        this(
                PluginUtils.isDevMode(pluginId) ? sandboxGitHubRepoUrl : productionGitHubRepoUrl,
                assignees,
                labels,
                actionText
        );
    }

    /**
     * Constructor for a {@link GitHubAndJetBrainsMarketplaceSubmitter}.
     *
     * @param gitHubRepoUrl The GitHub repository URL to use.
     * @param assignees     A list of assignees to add to the GitHub issue. Only works for authenticated users
     *                      that are maintainers of the GitHub repository.
     * @param labels        A list of labels to add to the GitHub issue. Only works for authenticated users
     *                      that are maintainers of the GitHub repository.
     * @param actionText    The text to display for the report action button.
     */

    public GitHubAndJetBrainsMarketplaceSubmitter(
            @NotNull final String gitHubRepoUrl,
            @Nullable final List<String> assignees,
            @Nullable final List<String> labels,
            @NotNull @NlsActions.ActionText final String actionText
    ) {
        super(
                List.of(
                        new GitHubErrorReportSubmitter(gitHubRepoUrl, assignees, labels),
                        new JetBrainsMarketplaceErrorReportSubmitter()
                ),
                true,
                actionText
        );
    }
}