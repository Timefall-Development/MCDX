package dev.timefall.mcdx.api;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class CleanlinessHelper {

	@SuppressWarnings("deprecation")
	public static final Random random = Random.createThreadSafe();

	public static boolean percentToOccur (int chance) {
		return random.nextInt(100) < chance;
	}

	public static void playCenteredSound (LivingEntity center, SoundEvent soundEvent, float volume, float pitch) {
		playCenteredSound(center, soundEvent, SoundCategory.PLAYERS, volume, pitch);
	}

	public static void playCenteredSound (LivingEntity center, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch) {
		center.getWorld().playSound(null,
				center.getX(), center.getY(), center.getZ(),
				soundEvent, soundCategory,
				volume, pitch);
	}

	public static void mcdx$dropItem(LivingEntity le, ItemStack itemStack) {
		ItemEntity it = new ItemEntity(
				le.getWorld(), le.getX(), le.getY(), le.getZ(),
				itemStack);
		le.getWorld().spawnEntity(it);
	}

	public static void mcdx$dropItem(LivingEntity le, Item item) {
		mcdx$dropItem(le, item, 1);
	}

	public static void mcdx$dropItem(LivingEntity le, Item item, int amount) {
		mcdx$dropItem(le, new ItemStack(item, amount));
	}

	public static TagKey<Item> mcdw$getItemTagKey(String tag) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of(tag));
	}

}