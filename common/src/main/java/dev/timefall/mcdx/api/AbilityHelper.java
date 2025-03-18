package dev.timefall.mcdx.api;

import dev.timefall.mcdx.configs.AoeExclusionType;
import dev.timefall.mcdx.configs.McdxCoreConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AbilityHelper {

	public static boolean canTarget(Entity attacker, Entity target, List<AoeExclusionType> exclusions) {
		return !(AoeExclusionType.isExcluded(attacker, target, exclusions));
	}

	public static boolean canTargetEnemy(Entity attacker, Entity target) {
		return !(AoeExclusionType.isExcluded(attacker, target, McdxCoreConfig.INSTANCE.allyExclusions));
	}

	public static boolean canTargetAlly(Entity attacker, Entity target) {
		//if an entity is excluded because of the ally exclusions, that means they ARE an ally
		return AoeExclusionType.isExcluded(attacker, target, McdxCoreConfig.INSTANCE.allyExclusions);
	}

	public static List<LivingEntity> getPotentialTargets(LivingEntity attacker, float distance, List<AoeExclusionType> exclusions) {
		return attacker.getEntityWorld().getEntitiesByClass(
				LivingEntity.class,
				new Box(attacker.getBlockPos()).expand(distance),
				nearbyEntity -> canTarget(attacker, nearbyEntity, exclusions)
		);
	}

	public static void applyToNearestTarget(LivingEntity attacker, float distance, List<AoeExclusionType> exclusions, Consumer<LivingEntity> nearestConsumer) {
		List<LivingEntity> nearbyEntities = getPotentialTargets(attacker, distance, exclusions);
		if (nearbyEntities.isEmpty()) return;
		LivingEntity closestEntity = nearbyEntities.stream()
				.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(attacker)))
				.get();
		nearestConsumer.accept(closestEntity);
	}

	public static void applyToNearestNTargets(LivingEntity attacker, int maxTargets, float distance, List<AoeExclusionType> exclusions, Consumer<LivingEntity> nearestConsumer) {
		List<LivingEntity> nearbyEntities = AbilityHelper.getPotentialTargets(attacker, distance, exclusions);
		if (nearbyEntities.isEmpty()) return;
		nearbyEntities.stream()
				.sorted(Comparator.comparingDouble(e -> e.squaredDistanceTo(attacker)))
				.limit(maxTargets)
				.forEach(nearestConsumer);
	}

	public static void applyToRandomNTargets(LivingEntity attacker, int maxTargets, float distance, List<AoeExclusionType> exclusions, Consumer<LivingEntity> nearestConsumer) {
		List<LivingEntity> nearbyEntities = AbilityHelper.getPotentialTargets(attacker, distance, exclusions);
		if (nearbyEntities.isEmpty()) return;
		List<LivingEntity> nearbyEntitiesCopy = new ArrayList<>(nearbyEntities);
		Collections.shuffle(nearbyEntitiesCopy);
		nearbyEntitiesCopy.stream()
				.limit(maxTargets)
				.forEach(nearestConsumer);
	}

	/*public static void stealSpeedFromTarget(LivingEntity user, LivingEntity target, int amplifier) {
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
	}*/
}