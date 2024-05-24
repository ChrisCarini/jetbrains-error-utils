# jetbrains-error-utils Notes

## Maven Central Release

- Example: https://github.com/minecraft-dev/annotations/blob/master/build.gradle.kts
-

## OSS Implementations of `com.intellij.openapi.diagnostic.ErrorReportSubmitter`

### Azure App Insights

**Search Results:** [GitHub Code Search]()

- https://cs.github.com/kenu/eclipse-github-copilot-integration/blob/2237ae268339711844d49bcdffefeb6995ecc059/src/com/github/copilot/telemetry/CopilotErrorHandler.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github
    - [Actual AAI usage](https://cs.github.com/kenu/eclipse-github-copilot-integration/blob/2237ae268339711844d49bcdffefeb6995ecc059/src/com/github/copilot/telemetry/AzureInsightsTelemetryService.java)

### GitHub

**Search Results:** [GitHub Code Search](https://cs.github.com/?scopeName=All+repos&scope=&q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)

- [applandinc/appmap-intellij-plugin](https://cs.github.com/applandinc/appmap-intellij-plugin/blob/07e3abf8bb5820a80f67441aa5ddd941b12ba483/src/main/java/appland/GitHubErrorHandler.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [pestphp/pest-intellij](https://cs.github.com/pestphp/pest-intellij/blob/283af3e53f58619d85f71d07463ae81b615bb6a1/src/main/kotlin/com/pestphp/pest/GithubErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [dmarks2/liferay-plugin-intellij](https://cs.github.com/dmarks2/liferay-plugin-intellij/blob/89ca85d24bf5de9bca96d14e3e22b9e7201dbe82/src/main/java/de/dm/intellij/liferay/util/PluginErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [KronicDeth/intellij-elixir](https://cs.github.com/KronicDeth/intellij-elixir/blob/23d0e06ded26aec26f2469da2de8b82ed51b9662/src/org/elixir_lang/errorreport/Submitter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [SchwarzIT/spectral-intellij-plugin](https://cs.github.com/SchwarzIT/spectral-intellij-plugin/blob/baf11367127c914c85d2e28482bd9b2c575a98de/src/main/java/com/schwarzit/spectralIntellijPlugin/util/ErrorReportSubmitter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [JetBrains/js-graphql-intellij-plugin](https://cs.github.com/JetBrains/js-graphql-intellij-plugin/blob/55ab62f4eef8c65f2fdb71e0fe031ec240d34d4c/src/main/com/intellij/lang/jsgraphql/ide/diagnostic/GraphQLGithubErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [JetBrains-Research/IntelliJDeodorant](https://cs.github.com/JetBrains-Research/IntelliJDeodorant/blob/df2196041d5e110fa712984a7ecb70936a3c29cf/src/main/java/org/jetbrains/research/intellijdeodorant/reporting/GitHubErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
    - Uses GitHub API w/ scrambled GH token; will comment on existing issue if one already exists.
- [wt-io-it/odoo-pycharm-plugin](https://cs.github.com/wt-io-it/odoo-pycharm-plugin/blob/c6ccfb0e946c384d4c693d483a1a67efd83f46eb/src/main/java/at/wtioit/intellij/plugins/odoo/errorHandling/PluginErrorHandler.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
- [JesusFreke/smalidea](https://cs.github.com/JesusFreke/smalidea/blob/23475c7f459ed3548d161701674600b7fdc20526/src/main/java/org/jf/smalidea/errorReporting/ErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
    - GitHub API w/ hidden token that is bundled
- [minecraft-dev/MinecraftDev](https://cs.github.com/minecraft-dev/MinecraftDev/blob/62e1c65ef2c5bc4cf78f73522a715abcf26c1ff2/src/main/kotlin/errorreporter/ErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
    - GitHub API; will comment on existing issues if one already created (even closed)
- [ml-in-programming/astrid](https://cs.github.com/ml-in-programming/astrid/blob/ac6b75716488eb0c3f7c5897f3f6a3f61dc83591/src/main/kotlin/reporting/GithubErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
    - Uses GitHub API w/ scrambled GH token; will comment on existing issue if one already exists.
- [ml-in-programming/ArchitectureReloaded](https://cs.github.com/ml-in-programming/ArchitectureReloaded/blob/287cee7752a1718ba6164c3cfb1ebf60d97c405f/src/main/java/org/jetbrains/research/groups/ml_methods/error_reporting/AnonymousFeedback.java)
    - Uses GitHub API w/ scrambled GH token; will comment on existing issue if one already exists.
- [karollewandowski/aem-intellij-plugin](https://github.com/karollewandowski/aem-intellij-plugin/blob/master/src/main/kotlin/co/nums/intellij/aem/errorreports/GitHubErrorReportSubmitter.kt)
    - Really neat! de-dups issues via hash; uses GitHub API w/ encrypted GH token; will comment on existing issue if one already exists.
    - Created by JB employee in OSS plugin

### Jira

**Search Results:** [GitHub Code Search](https://cs.github.com/?scopeName=All+repos&scope=&q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+jira)

- [fertroya/atlassian-clover](https://cs.github.com/fertroya/atlassian-clover/blob/5a2c8328fd9931f193d2f27199d1ce99cd7bdee4/clover-idea/src/com/atlassian/clover/idea/util/BlameClover.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+jira)
    - Simple Jira URL builder; not using auth'd API calls...

### Sentry.io

**Search Results:** [GitHub Code Search](https://cs.github.com/?scopeName=All+repos&scope=&q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)

- [lensvol/intellij-blackconnect](https://cs.github.com/lensvol/intellij-blackconnect/blob/1b44051f51233ea7988f1230e6e035d8e2681ed7/src/main/kotlin/me/lensvol/blackconnect/sentry/SentryErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [ksprojects/protobuf-jetbrains-plugin](https://cs.github.com/ksprojects/protobuf-jetbrains-plugin/blob/cd86013f9121274fc5b73136f8fb440813ed51e3/src/main/java/io/protostuff/jetbrains/plugin/errorreporting/SentryBugReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [shuzijun/markdown-editor](https://cs.github.com/shuzijun/markdown-editor/blob/9a33ccb7707ab11fadb46401bab47c8e37ceed8a/src/main/java/com/shuzijun/markdown/listener/ErrorReportHandler.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [melix/codenarc-idea](https://cs.github.com/melix/codenarc-idea/blob/fee81399acc71b618ddfa1c568604a3bceb80135/src/main/java/org/codenarc/idea/error/SentryErrorSubmitter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [JetBrains/js-graphql-intellij-plugin](https://cs.github.com/JetBrains/js-graphql-intellij-plugin/blob/55ab62f4eef8c65f2fdb71e0fe031ec240d34d4c/src/main/com/intellij/lang/jsgraphql/ide/diagnostic/GraphQLSentryErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [Unthrottled/themed-components](https://cs.github.com/Unthrottled/themed-components/blob/be1c72fe76f4f00740319114bdf2772b20b1c7b5/src/main/kotlin/io/unthrottled/themed/components/integrations/ErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [AxonFramework/IdeaPlugin](https://cs.github.com/AxonFramework/IdeaPlugin/blob/1b5c5d977d45761ef1905409bd877a229bbcb8a8/src/main/kotlin/org/axonframework/intellij/ide/plugin/support/AxonErrorReportSubmitter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [pgorsira/intellij-solidity](https://cs.github.com/pgorsira/intellij-solidity/blob/ff6c247b512493a4addcd5c52bdad98745eac626/src/main/kotlin/me/serce/solidity/ide/errors.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [asciidoctor/asciidoctor-intellij-plugin](https://cs.github.com/asciidoctor/asciidoctor-intellij-plugin/blob/791fc667d5bd917dda64ae720b8b0c7bf6c458f2/src/main/java/org/asciidoc/intellij/errorHandler/AsciiDocErrorHandler.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [intellij-dlanguage/intellij-dlanguage](https://cs.github.com/intellij-dlanguage/intellij-dlanguage/blob/bb757f273362c1683d0a811a20f860f234077228/errorreporting/src/main/java/io/github/intellij/dlanguage/errorreporting/DErrorReporter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [nocalhost/nocalhost-intellij-plugin](https://cs.github.com/nocalhost/nocalhost-intellij-plugin/blob/06770c23d867cdab37901e4f860e8342c5b398c2/src/main/java/dev/nocalhost/plugin/intellij/exception/report/NocalhostErrorReportSubmitter.java?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [DRSchlaubi/Intellij-GradleUpdater](https://cs.github.com/DRSchlaubi/Intellij-GradleUpdater/blob/5c19f10893bdb6c5116d51804a6d44a17e40f38a/src/main/kotlin/me/schlaubi/intellij_gradle_version_checker/error_handling/SentryErrorHandler.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [snyk/snyk-intellij-plugin](https://cs.github.com/snyk/snyk-intellij-plugin/blob/a9d48be85c4b08c3323f266f59c3155f64114c24/src/main/kotlin/snyk/errorHandler/SnykErrorReportSubmitter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [doki-theme/doki-theme-jetbrains](https://cs.github.com/doki-theme/doki-theme-jetbrains/blob/efac181edefe2c2ec83202987e68b755131c9196/src/main/kotlin/io/unthrottled/doki/integrations/ErrorReporter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [FirstTimeInForever/intellij-pdf-viewer](https://cs.github.com/FirstTimeInForever/intellij-pdf-viewer/blob/350b5676966732fac0a416fd8e4f8d8195a3474b/plugin/src/main/kotlin/com/firsttimeinforever/intellij/pdf/viewer/report/PdfErrorReportSubmitter.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+sentry)
- [pgorsira/intellij-solidity](https://cs.github.com/pgorsira/intellij-solidity/blob/ff6c247b512493a4addcd5c52bdad98745eac626/src/main/kotlin/me/serce/solidity/ide/errors.kt?q=com.intellij.openapi.diagnostic.ErrorReportSubmitter+github)
    - Will not send for dev / snapshot releases

#### Blog Post with Example

- https://www.plugin-dev.com/intellij/general/error-reporting/
- https://github.com/jansorg/intellij-error-reporting/blob/master/src/main/java/dev/ja/samples/errorReporting/SentryErrorReporter.java

## JetBrains Marketplace Exception Analyzer
```
2024-03-11 - Anna Maltceva - :jetbrains: - 9:39 AM
Dear Plugin Developers,
Exception Analyser beta version is now available on JetBrains Marketplace!
How It Works:
This new feature allows for exception reporting directly within JetBrains Marketplace. When users encounter an exception, they'll see the IDE Internal Errors dialog, offering them the option to report to the third-party plugin. This report will then be added to your exceptions listing for the plugin on JetBrains Marketplace. To see the Exception Analyzer report, navigate to your plugin's dashboard and select the Exceptions tab (see the screenshot attached).
How to Gain Early Access:
Express Your Interest:
Please send the email address associated with your Marketplace account via direct message, enabling us to activate this feature.
2. Implement Exception Reporting in Your Plugin:
Add the following entry to your plugin.xml file to enable the JetBrains Marketplace error handler:
<idea-plugin>
   <!--  ...  -->
  
   <extensions defaultExtensionNs="com.intellij">
       <errorHandler implementation="com.intellij.diagnostic.JetBrainsMarketplaceErrorReportSubmitter"/>
   </extensions>
</idea-plugin>
If you're using the IntelliJ Platform Plugin Template, update your gradle.properties file with the following configuration:
pluginSinceBuild = 233
pluginUntilBuild = 241.*
platformType = IC
platformVersion = 2023.3.4
We welcome any feedback on this feature and may reach out to you for further insights. Should you encounter any issues, please report them to our issue tracker: https://youtrack.jetbrains.com/issues/MP.
Looking forward to your participation and feedback!
```

- https://youtrack.jetbrains.com/issue/MP-6442/Make-JetBrainsMarketplaceErrorReportSubmitter-public-to-allow-extension