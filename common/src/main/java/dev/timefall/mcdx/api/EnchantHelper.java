package dev.timefall.mcdx.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class EnchantHelper {

    public static int getLevel(RegistryKey<Enchantment> registryKey, PlayerEntity playerEntity, int fallback) {
        Optional<? extends RegistryEntry<Enchantment>> entry = playerEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(registryKey);
        return entry.map(e -> EnchantmentHelper.getEquipmentLevel(e, playerEntity)).orElse(0);
    }

}
