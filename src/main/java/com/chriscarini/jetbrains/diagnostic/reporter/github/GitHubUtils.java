package com.chriscarini.jetbrains.diagnostic.reporter.github;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.github.authentication.GHAccountsUtil;
import org.jetbrains.plugins.github.authentication.accounts.GithubAccount;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for GitHub.
 */
public class GitHubUtils {

    /**
     * Extracts the username from a GitHub URL.
     *
     * @param url The GitHub repository URL.
     * @return The username.
     */
    @NotNull
    public static String extractUsername(@NotNull final String url) {
        Pattern pattern = Pattern.compile("github\\.com/([a-zA-Z0-9_-]+)(?:/([a-zA-Z0-9_.-]+))?");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "<unknown>";
        }
    }

    @Nullable
    protected static GithubAccount getReporterGithubAccount(@Nullable final Project project) {
        final Set<GithubAccount> accounts = GHAccountsUtil.getAccounts();
        final GithubAccount defaultAccount = project != null ? GHAccountsUtil.getDefaultAccount(project) : null;
        return defaultAccount != null ? defaultAccount : accounts.stream().findFirst().orElse(null);
    }

    protected static boolean isAnyValidGitHubReporterAccount() {
        return getReporterGithubAccount(null) != null;
    }

    /* Utility class has private constructor */
    private GitHubUtils() {
    }
}
