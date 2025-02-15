package dev.timefall.mcdx.api.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Rarity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiPredicate;

public interface EquipmentSet {

    String name();
    Rarity rarity();
    RegistryEntry<ArmorMaterial> material();
    Set<EquipmentSlot> slots();
    boolean isSetEquipped(LivingEntity user);

    default boolean isOf(EquipmentSet set) {
        return this == set;
    }

    default boolean isOf(EquipmentSet... sets) {
        for (EquipmentSet set : sets)
            if (this == set)
                return true;
        return false;
    }

    class Builder {

        private static final Set<EquipmentSlot> defaultSlots = EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

        public Builder(String name) {
            this.name = name;
        }

        private final String name;
        private Rarity rarity = Rarity.RARE;
        private RegistryEntry<ArmorMaterial> material = ArmorMaterials.IRON;
        private Set<EquipmentSlot> slots = defaultSlots;

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder material(RegistryEntry<ArmorMaterial> material) {
            this.material = material;
            return this;
        }

        public Builder slots(EquipmentSlot... slots) {
            this.slots = EnumSet.copyOf(Arrays.asList(slots));
            return this;
        }

        public EquipmentSet build(TagKey<Item> setTag) {
            return new Simple(this.name, this.rarity, this.material, this.slots, setTag);
        }

        public EquipmentSet build(BiPredicate<ItemStack, EquipmentSlot> predicate) {
            return new Predicated(this.name, this.rarity, this.material, this.slots, predicate);
        }

    }

    /// Basic Implementations /////////////

    record Simple(String name, Rarity rarity, RegistryEntry<ArmorMaterial> material, Set<EquipmentSlot> slots, TagKey<Item> setTag) implements EquipmentSet {

        @Override
        public boolean isSetEquipped(LivingEntity user) {
            for (EquipmentSlot slot : slots) {
                if (!user.getEquippedStack(slot).isIn(setTag)) return false;
            }
            return true;
        }
    }

    record Predicated(String name, Rarity rarity, RegistryEntry<ArmorMaterial> material, Set<EquipmentSlot> slots, BiPredicate<ItemStack, EquipmentSlot> predicate) implements EquipmentSet {

        @Override
        public boolean isSetEquipped(LivingEntity user) {
            for (EquipmentSlot slot : slots) {
                if (!predicate.test(user.getEquippedStack(slot), slot)) return false;
            }
            return true;
        }
    }
}
