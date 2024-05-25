package com.chriscarini.jetbrains.diagnostic.reporter.github;

import junit.framework.TestCase;


public class GitHubUtilsTest extends TestCase {
    public void testExtractUsername() {
        // given  
        final String githubUsername = "githubUsername";
        final String githubRepoUrl = String.format("https://github.com/%s/MY_COOL_REPO/", githubUsername);

        // when
        final String result = GitHubUtils.extractUsername(githubRepoUrl);

        // then
        assert result.equals(githubUsername);
    }

    public void testExtractUsername_InvalidGitHubUrl() {
        // given  
        final String githubRepoUrl = "https://jetbrains.chriscarini.com";

        // when
        final String result = GitHubUtils.extractUsername(githubRepoUrl);

        // then
        assert result.equals("<unknown>");
    }
}
