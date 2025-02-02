/*
 * Timefall Development License 1.2
 * Copyright (c) 2020-2024. Chronosacaria, Kluzzio, Timefall Development. All Rights Reserved.
 *
 * This software's content is licensed under the Timefall Development License 1.2. You can find this license information here: https://github.com/Timefall-Development/Timefall-Development-Licence/blob/main/TimefallDevelopmentLicense1.2.txt
 */
package dev.timefall.mcdx.configs;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.loader.api.FabricLoader;

public class CompatibilityFlags {
    public static boolean noOffhandConflicts = true;
    public static boolean isReachExtensionEnabled = true;

    public static void init() {
        if(ConfigApiJava.platform().isModLoaded("dualwielding") || ConfigApiJava.platform().isModLoaded("bettercombat")) {
            noOffhandConflicts = false;
        }
        if(ConfigApiJava.platform().isModLoaded("bettercombat")) {
            isReachExtensionEnabled = false;
        }
    }
}