package dev.timefall.mcdx.api;

import dev.timefall.mcdx.api.weapon.RangedAttackHelper;
import dev.timefall.mcdx.configs.McdxCoreConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

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

    @SuppressWarnings("SuspiciousNameCombination")
    public static void mcdx$setProjectileTowards(ProjectileEntity projectileEntity, double x, double y, double z, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z))
                .normalize()
                .add(
                        CleanlinessHelper.random.nextGaussian() * 0.0075 * inaccuracy,
                        CleanlinessHelper.random.nextGaussian() * 0.0075 * inaccuracy,
                        CleanlinessHelper.random.nextGaussian() * 0.0075 * inaccuracy);
        projectileEntity.setVelocity(vec3d);
        float f = MathHelper.sqrt((float)projectileEntity.squaredDistanceTo(vec3d));
        projectileEntity.setYaw((float)(MathHelper.atan2(x, z) * (180.0 / Math.PI)));
        projectileEntity.setPitch((float)(MathHelper.atan2(vec3d.y, f) * (180.0 / Math.PI)));
        projectileEntity.prevYaw = projectileEntity.getYaw();
        projectileEntity.prevPitch = projectileEntity.getPitch();
    }

    public static void mcdx$setProjectileTowards(ProjectileEntity projectileEntity, double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z).normalize();
        projectileEntity.setVelocity(vec3d);
        float f = MathHelper.sqrt((float) projectileEntity.squaredDistanceTo(vec3d));
        //noinspection SuspiciousNameCombination
        projectileEntity.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * (180d / Math.PI)));
        projectileEntity.setPitch((float) (MathHelper.atan2(vec3d.y, f) * (180d / Math.PI)));
        projectileEntity.prevYaw = projectileEntity.getYaw();
        projectileEntity.prevPitch = projectileEntity.getPitch();
    }

    private static void fireProjectileAtNearbyEnemies(ProjectileEntity projectileEntity, LivingEntity user, float distance) {
        AbilityHelper.applyToNearestTarget(user, distance, McdxCoreConfig.INSTANCE.allyExclusions, nearestEntity -> {
            double d = nearestEntity.getX() - projectileEntity.getX();
            double e = nearestEntity.getBodyY(0.3333333333333333D) - projectileEntity.getY();
            double f = nearestEntity.getZ() - projectileEntity.getZ();
            double g = Math.sqrt(d * d + f * f);
            projectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            mcdx$setProjectileTowards(projectileEntity, d, e, g, 0);
            user.getWorld().spawnEntity(projectileEntity);
        });
    }

    public static void mcdx$spawnExtraArrows(LivingEntity owner, LivingEntity makeArrowFromMe, @Nullable ItemStack weapon, int numArrowsLimit, float distance, double bonusShotDamageMultiplier) {
        List<LivingEntity> nearbyEntities = RangedAttackHelper.mcdx$getSecondaryTargets(makeArrowFromMe, distance);
        for (int i = 0; i < Math.min(numArrowsLimit, nearbyEntities.size()); i++) {
            PersistentProjectileEntity arrowEntity = mcdx$fireProjectileAtTarget(makeArrowFromMe, nearbyEntities.get(i), mcdx$createAbstractArrow(owner, weapon), 1, 0);
            arrowEntity.setDamage(arrowEntity.getDamage() * bonusShotDamageMultiplier);
            arrowEntity.setOwner(owner);
            makeArrowFromMe.getWorld().spawnEntity(arrowEntity);
        }
    }

    public static PersistentProjectileEntity mcdx$createAbstractArrow(LivingEntity attacker, @Nullable ItemStack weapon) {
        return ((ArrowItem) Items.ARROW).createArrow(attacker.getEntityWorld(), new ItemStack(Items.ARROW), attacker, weapon);
    }

    public static <T extends ProjectileEntity> T mcdx$fireProjectileAtTarget(Entity source, Entity target, T projectile, float power, float uncertainty, BiFunction<Double, Double, Double> yDirectionModifier) {
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

    public static <T extends ProjectileEntity> T mcdx$fireProjectileAtTarget(Entity source, Entity target, T projectile, float power, float uncertainty) {
        return mcdx$fireProjectileAtTarget(source, target, projectile, power, uncertainty, (x, z) -> MathHelper.hypot(x, z) * 0.2);
    }

}
