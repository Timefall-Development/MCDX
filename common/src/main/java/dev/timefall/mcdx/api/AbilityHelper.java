package dev.timefall.mcdx.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class AbilityHelper {

	public static void stealSpeedFromTarget(LivingEntity user, LivingEntity target, int amplifier) {
		stealSpeedFromTarget(user, target, amplifier, 80);
	}

	public static void stealSpeedFromTarget(LivingEntity user, LivingEntity target, int amplifier, int duration) {
		StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, duration, amplifier);
		StatusEffectInstance slowness = new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier);
		user.addStatusEffect(speed);
		target.addStatusEffect(slowness);
	}

	public static void causeFreezing(LivingEntity target, int amplifier) {
		causeFreezing(target, amplifier, 60);
	}

	public static void causeFreezing(LivingEntity target, int amplifier, int duration) {
		StatusEffectInstance freezing = new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier);
		StatusEffectInstance miningFatigue = new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, amplifier);
		target.addStatusEffect(freezing);
		target.addStatusEffect(miningFatigue);
	}



}