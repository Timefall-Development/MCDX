package dev.timefall.mcdx.api;

import dev.timefall.mcdx.configs.AoeExclusionType;
import dev.timefall.mcdx.configs.McdxCoreConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class AOEHelper {

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

	public static List<LivingEntity> getEntitiesWithExclusions(LivingEntity centerEntity, float distance, Collection<AoeExclusionType> exclusions) {
		return getEntitiesByPredicate(centerEntity, distance, targetEntity ->
				isAoeTarget(centerEntity, targetEntity, exclusions)
		);
	}

	public static List<LivingEntity> getEntitiesWithExclusions(LivingEntity centerEntity, LivingEntity causeEntity, float distance, Collection<AoeExclusionType> exclusions) {
		return getEntitiesByPredicate(centerEntity, distance, targetEntity ->
				isAoeTarget(centerEntity, causeEntity, targetEntity, exclusions)
		);
	}

	public static boolean isAoeTarget(LivingEntity self, LivingEntity foreignEntity, Collection<AoeExclusionType> exclusions) {
		return isAoeTarget(self, self, foreignEntity, exclusions);
	}

	public static boolean isAoeTarget(LivingEntity center, LivingEntity cause, LivingEntity foreignEntity, Collection<AoeExclusionType> exclusions) {
		if (AoeExclusionType.isExcluded(cause, center, foreignEntity, exclusions)) return false;
		return foreignEntity.isAlive()
				&& isAffectedByAoe(foreignEntity);
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

	////////////////////
	public static void summonLightningBoltOnEntity(Entity target){
		World world = target.getEntityWorld();
		LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
		if (lightningEntity != null) {
			lightningEntity.refreshPositionAfterTeleport(target.getX(), target.getY(), target.getZ());
			lightningEntity.setCosmetic(true);
			target.onStruckByLightning((ServerWorld) world, lightningEntity);
			world.spawnEntity(lightningEntity);
		}
	}

	public static void electrocute(LivingEntity victim, float damageAmount){
		summonLightningBoltOnEntity(victim);
		victim.damage(victim.getWorld().getDamageSources().lightningBolt(), damageAmount);
	}

	public static void electrocuteNearbyEnemies(LivingEntity cause, LivingEntity center, float distance, float damageAmount, int limit){
		CleanlinessHelper.playCenteredSound(cause, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 1.0F, 1.0F);
		CleanlinessHelper.playCenteredSound(cause, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 1.0F, 1.0F);

		for (LivingEntity nearbyEntity : getEntitiesByConfig(cause, center, distance)) {
			electrocute(nearbyEntity, damageAmount);

			limit--;
			if (limit <= 0) break;
		}
	}

	///////////////
	public static void knockbackNearbyEnemies(PlayerEntity user, LivingEntity nearbyEntity, float knockbackMultiplier) {
		double xRatio = user.getX() - nearbyEntity.getX();
		double zRatio;
		for (
				zRatio = user.getZ() - nearbyEntity.getZ();
				xRatio * xRatio + zRatio < 1.0E-4D;
				zRatio = (CleanlinessHelper.random.nextDouble() - CleanlinessHelper.random.nextDouble()) * 0.01D) {
			xRatio = (CleanlinessHelper.random.nextDouble() - CleanlinessHelper.random.nextDouble()) * 0.01D;
		}
		nearbyEntity.takeKnockback(0.4F * knockbackMultiplier, xRatio, zRatio);
	}

}