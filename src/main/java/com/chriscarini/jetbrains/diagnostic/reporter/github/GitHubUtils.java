package com.chriscarini.jetbrains.diagnostic.reporter.github;

import org.jetbrains.annotations.NotNull;

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

    /* Utility class has private constructor */
    private GitHubUtils() {
    }
}
