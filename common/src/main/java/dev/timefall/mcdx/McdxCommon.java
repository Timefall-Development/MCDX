package dev.timefall.mcdx;

import dev.timefall.mcdx.platform.Services;

public class McdxCommon {

   public static void initialize() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this timefall
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("mcdx")) {

            ModConstants.LOG.info("Hello to mcdx");
        }
    }
}