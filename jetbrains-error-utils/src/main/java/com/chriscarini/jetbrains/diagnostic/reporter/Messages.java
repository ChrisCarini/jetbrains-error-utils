package com.chriscarini.jetbrains.diagnostic.reporter;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;


/**
 * Localized messages for LoC COP.
 */
public final class Messages {
    @NonNls
    private static final String BUNDLE = "messages.jetbrains-error-utils";
    private static Reference<ResourceBundle> bundle;

    private Messages() {
    }

    /**
     * Gets a localized message from the bundle.
     * @param key the key to look up
     * @param params the parameters to use in the message
     * @return the localized message
     */
    public static String message(@NotNull @NonNls @PropertyKey(resourceBundle = BUNDLE) final String key, @NotNull final Object... params) {
        return AbstractBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = com.intellij.reference.SoftReference.dereference(Messages.bundle);
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            Messages.bundle = new SoftReference<>(bundle);
        }
        return bundle;
    }
}
