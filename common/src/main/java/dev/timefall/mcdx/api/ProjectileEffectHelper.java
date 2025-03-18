package dev.timefall.mcdx.api;

import dev.timefall.mcdx.api.weapon.RangedAttackHelper;
import dev.timefall.mcdx.configs.McdxCoreConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ProjectileEffectHelper {

    public static void ricochetArrowLikeShield(PersistentProjectileEntity persistentProjectileEntity){
        persistentProjectileEntity.setVelocity(persistentProjectileEntity.getVelocity().multiply(-0.1D));
        persistentProjectileEntity.getYaw(180.0F);
        persistentProjectileEntity.prevYaw += 180.0F;
        if (!persistentProjectileEntity.getEntityWorld().isClient && persistentProjectileEntity.getVelocity().lengthSquared() < 1.0E-7D){
            if (persistentProjectileEntity.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED){
                persistentProjectileEntity.dropStack(new ItemStack(Items.ARROW), 0.1F);
            }
            persistentProjectileEntity.remove(Entity.RemovalReason.KILLED);
        }
    }

    public static void mcdx$spawnExtraArrows(LivingEntity owner, LivingEntity makeArrowFromMe, @Nullable ItemStack weapon, int numArrowsLimit, float distance, double bonusShotDamageMultiplier) {
        mcdx$fireProjectileAtNearestNEnemies(() -> {
            PersistentProjectileEntity ae = mcdx$createAbstractArrow(owner, weapon);
            ae.setDamage(ae.getDamage() * bonusShotDamageMultiplier);
            ae.setOwner(owner);
            return ae;
        }, makeArrowFromMe, numArrowsLimit, distance, 1f, 0f);
    }

    public static PersistentProjectileEntity mcdx$createAbstractArrow(LivingEntity attacker, @Nullable ItemStack weapon) {
        return ((ArrowItem) Items.ARROW).createArrow(attacker.getEntityWorld(), new ItemStack(Items.ARROW), attacker, weapon);
    }

    public static <T extends ProjectileEntity> T mcdx$pointProjectileAtTarget(Entity source, Entity target, T projectile, float power, float uncertainty, BiFunction<Double, Double, Double> yDirectionModifier) {
        //PersistentProjectileEntity projectile = mcdx$createAbstractArrow(source, weapon);
        // borrowed from AbstractSkeletonEntity
        double towardsX = target.getX() - source.getX();
        double towardsZ = target.getZ() - source.getZ();
        //double euclideanDist = MathHelper.hypot(towardsX, towardsZ); // I'm assuming this is to "arc" the projectile
        //0.333333D fires at the legs... is that intentional?
        double towardsY = target.getBodyY(0.3333333333333333D) - projectile.getY() + yDirectionModifier.apply(towardsX, towardsZ);
        projectile.setVelocity(towardsX, towardsY, towardsZ, power, uncertainty);
        if (projectile instanceof PersistentProjectileEntity ppe)
            ppe.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        return projectile;
    }

    public static <T extends ProjectileEntity> T mcdx$pointProjectileAtTarget(Entity source, Entity target, T projectile, float power, float uncertainty) {
        return mcdx$pointProjectileAtTarget(source, target, projectile, power, uncertainty, (x, z) -> MathHelper.hypot(x, z) * 0.2);
    }

    private static void mcdx$fireProjectileAtNearestEnemy(ProjectileEntity projectileEntity, LivingEntity user, float searchRadius, float power, float uncertainty, BiFunction<Double, Double, Double> yDirectionModifier) {
        AbilityHelper.applyToNearestTarget(user, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectileEntity, power, uncertainty, yDirectionModifier);
            user.getWorld().spawnEntity(projectileEntity);
        });
    }

    private static void mcdx$fireProjectileAtNearestEnemy(ProjectileEntity projectileEntity, LivingEntity user, float searchRadius, float power, float uncertainty) {
        AbilityHelper.applyToNearestTarget(user, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectileEntity, power, uncertainty);
            user.getWorld().spawnEntity(projectileEntity);
        });
    }

    private static void mcdx$fireProjectileAtNearestNEnemies(Supplier<ProjectileEntity> projectileEntity, LivingEntity user, int maxTargets, float searchRadius, float power, float uncertainty, BiFunction<Double, Double, Double> yDirectionModifier) {
        AbilityHelper.applyToNearestNTargets(user, maxTargets, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            ProjectileEntity projectile = projectileEntity.get();
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectile, power, uncertainty, yDirectionModifier);
            user.getWorld().spawnEntity(projectile);
        });
    }

    private static void mcdx$fireProjectileAtNearestNEnemies(Supplier<ProjectileEntity> projectileEntity, LivingEntity user, int maxTargets, float searchRadius, float power, float uncertainty) {
        AbilityHelper.applyToNearestNTargets(user, maxTargets, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            ProjectileEntity projectile = projectileEntity.get();
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectile, power, uncertainty);
            user.getWorld().spawnEntity(projectile);
        });
    }

    private static void mcdx$fireProjectileAtRandomNEnemies(Supplier<ProjectileEntity> projectileEntity, LivingEntity user, int maxTargets, float searchRadius, float power, float uncertainty, BiFunction<Double, Double, Double> yDirectionModifier) {
        AbilityHelper.applyToRandomNTargets(user, maxTargets, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            ProjectileEntity projectile = projectileEntity.get();
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectile, power, uncertainty, yDirectionModifier);
            user.getWorld().spawnEntity(projectile);
        });
    }

    private static void mcdx$fireProjectileAtRandomNEnemies(Supplier<ProjectileEntity> projectileEntity, LivingEntity user, int maxTargets, float searchRadius, float power, float uncertainty) {
        AbilityHelper.applyToRandomNTargets(user, maxTargets, searchRadius, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            ProjectileEntity projectile = projectileEntity.get();
            mcdx$pointProjectileAtTarget(user, nearestEntity, projectile, power, uncertainty);
            user.getWorld().spawnEntity(projectile);
        });
    }
}
