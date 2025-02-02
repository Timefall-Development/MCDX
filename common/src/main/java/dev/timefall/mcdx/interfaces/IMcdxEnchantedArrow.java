/*
 * Timefall Development License 1.2
 * Copyright (c) 2020-2024. Chronosacaria, Kluzzio, Timefall Development. All Rights Reserved.
 *
 * This software's content is licensed under the Timefall Development License 1.2. You can find this license information here: https://github.com/Timefall-Development/Timefall-Development-Licence/blob/main/TimefallDevelopmentLicense1.2.txt
 */
package dev.timefall.mcdx.interfaces;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

public interface IMcdxEnchantedArrow {

    int mcdx$getEnchantmentLevel(RegistryKey<Enchantment> enchantment);

    void mcdx$applyEnchantment(RegistryKey<Enchantment> enchantment, int level);
}