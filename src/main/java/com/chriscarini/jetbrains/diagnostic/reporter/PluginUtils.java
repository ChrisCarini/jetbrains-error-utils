package com.chriscarini.jetbrains.diagnostic.reporter;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Utility methods to obtain information Plugins.
 */
public final class PluginUtils {

    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

    /**
     * Indicates whether the plugin is an official build or a developer build.
     *
     * @param pluginId the name of the plugin
     * @return {@code true} if the plugin is a developer build. A developer build is identified if any of the following
     * are true:
     * - the plugin version ends with "-SNAPSHOT"
     * - the plugin path contains `build/idea-sandbox`
     * - a plugin version could not be determined
     */
    public static boolean isDevMode(final String pluginId) {
        return isDevMode(getPlugin(pluginId));
    }

    private static boolean isDevMode(final IdeaPluginDescriptor pluginDesc) {
        if (pluginDesc == null) {
            return true;
        }

        final String pluginVersion = pluginDesc.getVersion();
        return pluginVersion == null ||
                pluginVersion.endsWith(SNAPSHOT_SUFFIX) ||
                pluginDesc.getPluginPath().toString().contains("build/idea-sandbox");
    }

    /**
     * Identifies if the given plugin is installed.
     *
     * @param pluginId the name of the plugin
     * @return true if the plugin is installed; false if the plugin is not installed
     */
    public static boolean isPluginInstalled(@NotNull final String pluginId) {
        final PluginId id = getPluginId(pluginId);
        return PluginManager.isPluginInstalled(id);
    }

    /**
     * Identifies if the given plugin is enabled.
     *
     * @param pluginId the name of the plugin
     * @return true if the plugin is enabled; false if the plugin is not enabled
     */
    public static boolean isPluginEnabled(@NotNull final String pluginId) {
        @Nullable final IdeaPluginDescriptor pluginDescriptor = getPlugin(pluginId);
        return pluginDescriptor != null && pluginDescriptor.isEnabled();
    }

    private static @NotNull PluginId getPluginId(@NotNull final String pluginId) {
        return PluginId.getId(pluginId);
    }

    private static IdeaPluginDescriptor getPlugin(@NotNull final String pluginId) {
        return PluginManagerCore.getPlugin(getPluginId(pluginId));
    }

    /* Utility class has private constructor */
    private PluginUtils() {
    }
}
