package dev.timefall.mcdx.api;

import dev.timefall.mcdx.configs.AoeExclusionType;
import dev.timefall.mcdx.configs.McdxCoreConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class AoeHelper {

	/** Returns targets of an AOE statusEffect from 'attacker' around 'center'. This includes 'center'. */
	public static List<LivingEntity> getEntitiesByPredicate(LivingEntity centerEntity, float distance, Predicate<? super LivingEntity> predicate) {
		return getEntitiesByPredicate(LivingEntity.class, centerEntity, distance, predicate);
	}

	public static <T extends LivingEntity> List<T> getEntitiesByPredicate(Class<T> entityType, LivingEntity centerEntity, float distance, Predicate<? super LivingEntity> predicate) {
		return centerEntity.getEntityWorld().getEntitiesByClass(entityType, new Box(centerEntity.getBlockPos()).expand(distance), predicate);
	}

	public static List<LivingEntity> getEntitiesByConfig(LivingEntity centerEntity, float distance) {
		return getEntitiesByPredicate(centerEntity, distance, targetEntity ->
				isAoeTarget(centerEntity, targetEntity, McdxCoreConfig.INSTANCE.aoeExclusions)
		);
	}

	public static List<LivingEntity> getEntitiesByConfig(LivingEntity centerEntity, LivingEntity causeEntity, float distance) {
		return getEntitiesByPredicate(centerEntity, distance, targetEntity ->
				isAoeTarget(centerEntity, causeEntity, targetEntity, McdxCoreConfig.INSTANCE.aoeExclusions)
		);
	}

	public static boolean isAoeTarget(LivingEntity self, LivingEntity foreignEntity, Collection<AoeExclusionType> exclusions) {
		return isAoeTarget(self, self, foreignEntity, exclusions);
	}

	public static boolean isAoeTarget(LivingEntity center, LivingEntity cause, LivingEntity foreignEntity, Collection<AoeExclusionType> exclusions) {
		if (!AoeExclusionType.applies(cause, foreignEntity, exclusions)) return false;
		return foreignEntity.isAlive()
				&& isAffectedByAoe(foreignEntity)
				&& center.canSee(foreignEntity);
	}

	private static boolean isAffectedByAoe(LivingEntity entity) {
		if (entity instanceof PlayerEntity player) {
			return !(player.isCreative() || player.isSpectator());
		}
		return true;
	}

	///////////////////////

	public static void afflictNearbyEntities(LivingEntity user, float distance, StatusEffectInstance... statusEffectInstances) {
		for (LivingEntity nearbyEntity : getEntitiesByConfig(user, distance)) {
			for (StatusEffectInstance instance : statusEffectInstances)
				nearbyEntity.addStatusEffect(instance);
		}
	}

	public static boolean satisfySweepConditions(LivingEntity attackingEntity, Entity targetEntity, LivingEntity collateralEntity, float distanceToCollateral) {
		return collateralEntity != attackingEntity && collateralEntity != targetEntity && !attackingEntity.isTeammate(collateralEntity)
				&& !(collateralEntity instanceof ArmorStandEntity armorStand && armorStand.isMarker())
				&& attackingEntity.distanceTo(collateralEntity) < distanceToCollateral;
	}

}