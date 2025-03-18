package dev.timefall.mcdx.api.weapon;

import dev.timefall.mcdx.api.AOEHelper;
import dev.timefall.mcdx.api.AbilityHelper;
import dev.timefall.mcdx.api.CleanlinessHelper;
import dev.timefall.mcdx.api.EnchantHelper;
import dev.timefall.mcdx.configs.CompatibilityFlags;
import dev.timefall.mcdx.interfaces.IDualWielding;
import dev.timefall.mcdx.interfaces.IMixAndMatchItem;
import dev.timefall.mcdx.interfaces.IOffhandAttack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class PlayerAttackHelper {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean mcdx$isLikelyNotMeleeDamage(DamageSource damageSource){
        return damageSource.isOf(DamageTypes.ON_FIRE)
                || damageSource.isOf(DamageTypes.EXPLOSION)
                || damageSource.isOf(DamageTypes.MAGIC)
                || damageSource.isOf(DamageTypes.ARROW)
                || !mcdx$isDirectDamage(damageSource);
    }

    private static boolean mcdx$isDirectDamage(DamageSource damageSource){
        return damageSource.isOf(DamageTypes.MOB_ATTACK)
                || damageSource.isOf(DamageTypes.PLAYER_ATTACK);
    }

    //public static void mcdx$switchModifiers(PlayerEntity player, ItemStack switchFrom, ItemStack switchTo) {
    //    player.getAttributes().removeModifiers(switchFrom.getAttributeModifiers(EquipmentSlot.MAINHAND));
    //    player.getAttributes().addTemporaryModifiers(switchTo.getAttributeModifiers(EquipmentSlot.MAINHAND));
    //}

    public static void mcdx$offhandAttack(PlayerEntity playerEntity, Entity target) {
        if (!CompatibilityFlags.noOffhandConflicts) return;
        if (!target.isAttackable())
            if (target.handleAttack(playerEntity))
                return;

        ItemStack offhandStack = playerEntity.getOffHandStack();

        // use offhand modifiers
        //mcdx$switchModifiers(playerEntity, playerEntity.getMainHandStack(), offhandStack);
        DamageSource damageSource = playerEntity.getWorld().getDamageSources().playerAttack(playerEntity);
        float cooldownProgress = ((IDualWielding) playerEntity).mcdx$getOffhandAttackCooldownProgress(0.5F);
        float baseAttackDamage = (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float bonusAttackDamage = playerEntity instanceof ServerPlayerEntity spe
                ? EnchantmentHelper.getDamage(spe.getServerWorld(), offhandStack, target, damageSource, baseAttackDamage) - baseAttackDamage
                : 0;

        baseAttackDamage *= (0.2f + cooldownProgress * cooldownProgress * 0.8f);
        bonusAttackDamage *= cooldownProgress;

        if (baseAttackDamage == 0 && bonusAttackDamage == 0) return;

        //not needed in 1.21
        // use mainhand modifiers
        //mcdx$switchModifiers(playerEntity, offhandStack, playerEntity.getMainHandStack());

        baseAttackDamage += offhandStack.getItem().getBonusAttackDamage(target, baseAttackDamage, damageSource);


        //no longer needed in 1.21, enchanted cdamage bonus is attribute value added directly to player
        //float enchantBonusDamage = EnchantmentHelper.getAttackDamage(offhandStack, target instanceof LivingEntity livingTarget ?
        //        livingTarget.getGroup() : EntityGroup.DEFAULT) * cooldownProgress;

        ((IDualWielding) playerEntity).mcdx$resetLastAttackedOffhandTicks();

        /* bl */
        boolean isMostlyCharged = cooldownProgress > 0.9f;

        /* i */
        int knockbackLevel = EnchantHelper.getLevel(Enchantments.KNOCKBACK, playerEntity, 0);
        if (playerEntity.isSprinting() && isMostlyCharged) {
            CleanlinessHelper.playCenteredSound(playerEntity, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.0f, 1.0f);
            ++knockbackLevel;
        }

        boolean playerShouldCrit = isMostlyCharged && entityCanCrit(playerEntity) && target instanceof LivingEntity;
        if (playerShouldCrit && !playerEntity.isSprinting()) {
            baseAttackDamage *= 1.5f;
        }

        float attackDamage = baseAttackDamage + bonusAttackDamage;
        boolean playerShouldSweep = isMostlyCharged && !playerShouldCrit && !playerEntity.isSprinting() && playerEntity.isOnGround()
                && playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed < (double) playerEntity.getMovementSpeed()
                && offhandStack.getItem() instanceof IOffhandAttack;

        /* j */
        float targetHealth = 0.0f;
        boolean bl5 = false;
        /* k */
        int fireAspectLevel = EnchantHelper.getLevel(Enchantments.FIRE_ASPECT, playerEntity, 0);
        if (target instanceof LivingEntity livingTarget) {
            targetHealth = livingTarget.getHealth();
            if (fireAspectLevel > 0 && !livingTarget.isOnFire()) {
                bl5 = true;
                livingTarget.setOnFireFor(1);
            }
        }

        Vec3d targetVelocity = target.getVelocity();
        if (target.damage(target.getWorld().getDamageSources().playerAttack(playerEntity), attackDamage)) {
            double positionOne = -MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180));
            double positionTwo = MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180));
            if (knockbackLevel > 0) {
                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.takeKnockback((float) knockbackLevel * 0.5f, -positionOne, -positionTwo);
                } else {
                    target.addVelocity(positionOne * (float) knockbackLevel * 0.5f, 0.1,
                            positionTwo * (float) knockbackLevel * 0.5f);
                }
                playerEntity.setVelocity(playerEntity.getVelocity().multiply(0.6, 1.0, 0.6));
                playerEntity.setSprinting(false);
            }

            if (playerShouldSweep) {
                float sweepingEdgeMultiplierTimesDamage = 1.0F + (float)playerEntity.getAttributeValue(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO) * baseAttackDamage;
                playerEntity.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.0, 0.25, 1.0)).forEach(sweptEntity -> {
                    if (AOEHelper.satisfySweepConditions(playerEntity, target, sweptEntity, 3.0f)) {
                        sweptEntity.takeKnockback(0.4f, -positionOne, -positionTwo);
                        float sweepDamage = playerEntity instanceof ServerPlayerEntity spe
                                ? EnchantmentHelper.getDamage(spe.getServerWorld(), offhandStack, sweptEntity, damageSource, sweepingEdgeMultiplierTimesDamage)
                                : sweepingEdgeMultiplierTimesDamage;
                        sweptEntity.damage(damageSource, sweepDamage);
                        if (playerEntity.getWorld() instanceof ServerWorld serverWorld) {
                            EnchantmentHelper.onTargetDamaged(serverWorld, sweptEntity, damageSource);
                        }
                    }
                });
                CleanlinessHelper.playCenteredSound(playerEntity, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
                playerEntity.spawnSweepAttackParticles();
                /*if (playerEntity.getWorld() instanceof ServerWorld serverWorld) {
                    //serverWorld.spawnParticles(ParticlesRegistry.OFFHAND_SWEEP_PARTICLE, playerEntity.getX() + positionOne,
                    //        playerEntity.getBodyY(0.5D), playerEntity.getZ() + positionTwo, 0, positionOne, 0.0D, positionTwo, 0.0D);
                }*/

            }

            if (target instanceof ServerPlayerEntity && target.velocityModified) {
                ((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
                target.velocityModified = false;
                target.setVelocity(targetVelocity);
            }
            if (playerShouldCrit) {
                CleanlinessHelper.playCenteredSound(playerEntity, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
                playerEntity.addCritParticles(target);
            } else if (!playerShouldSweep) {
                CleanlinessHelper.playCenteredSound(playerEntity,
                        isMostlyCharged ? SoundEvents.ENTITY_PLAYER_ATTACK_STRONG : SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                        1.0f, 1.0f);
            }

            if (bonusAttackDamage > 0.0f) {
                playerEntity.addEnchantedHitParticles(target);
            }

            playerEntity.onAttacking(target);
            Entity entity = target;
            if (target instanceof EnderDragonPart) {
                entity = ((EnderDragonPart)target).owner;
            }

            boolean bl6 = false;
            if (playerEntity.getWorld() instanceof ServerWorld serverWorld2) {
                if (!offhandStack.isEmpty() && entity instanceof LivingEntity livingEntity3x) {
                    bl6 = offhandStack.postHit(livingEntity3x, playerEntity);

                }
                EnchantmentHelper.onTargetDamaged(serverWorld2, target, damageSource);
            }

            if (!playerEntity.getWorld().isClient && !offhandStack.isEmpty() && entity instanceof LivingEntity) {
                if (bl6) {
                    offhandStack.postDamageEntity((LivingEntity)entity, playerEntity);
                }
                if (offhandStack.isEmpty()) {
                    playerEntity.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                }
            }

            if ( target instanceof LivingEntity livingTarget) {
                /* m */
                float targetCurrentHealth = targetHealth - livingTarget.getHealth();
                playerEntity.increaseStat(Stats.DAMAGE_DEALT, Math.round(targetCurrentHealth * 10.0f));
                if (fireAspectLevel > 0) {
                    target.setOnFireFor(fireAspectLevel * 4);
                }

                if (playerEntity.getWorld() instanceof ServerWorld playerServerWorld && targetCurrentHealth > 2.0f) {
                    int particleCount = (int) ((double) targetCurrentHealth * 0.5);
                    playerServerWorld.spawnParticles(ParticleTypes.DAMAGE_INDICATOR,
                            target.getX(), target.getBodyY(0.5), target.getZ(),
                            particleCount, 0.1, 0.0, 0.1, 0.2);
                }
            }
            playerEntity.addExhaustion(0.1f);
        } else {
            CleanlinessHelper.playCenteredSound(playerEntity, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
            if (bl5) {
                target.extinguish();
            }
        }
    }

    public static boolean mixAndMatchWeapons(PlayerEntity playerEntity) {
        return (playerEntity.getOffHandStack().isOf(playerEntity.getMainHandStack().getItem())
                || (playerEntity.getMainHandStack().getItem() instanceof IMixAndMatchItem item && item.offhandItemIsValid(playerEntity.getOffHandStack().getItem())));
    }

    public static boolean entityCanCrit(LivingEntity livingEntity) {
        return !livingEntity.isClimbing()
                && !livingEntity.isTouchingWater()
                && !livingEntity.isOnGround()
                && !livingEntity.isSprinting()
                && !livingEntity.hasVehicle()
                && !livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)
                && livingEntity.fallDistance > 0;
    }

}
