/*
 * Timefall Development License 1.2
 * Copyright (c) 2020-2024. Chronosacaria, Kluzzio, Timefall Development. All Rights Reserved.
 *
 * This software's content is licensed under the Timefall Development License 1.2. You can find this license information here: https://github.com/Timefall-Development/Timefall-Development-Licence/blob/main/TimefallDevelopmentLicense1.2.txt
 */
package dev.timefall.mcdx.api;

import dev.timefall.mcdx.configs.AoeExclusionType;
import dev.timefall.mcdx.interfaces.IExclusiveAOECloud;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AOECloudHelper {

    public static void spawnAreaEffectCloudEntityWithAttributes(LivingEntity user, LivingEntity center, float cloudRadius,
                                                                int cloudWaitTime, int cloudDuration,
                                                                RegistryEntry<StatusEffect> statusEffect, int effectDuration, int effectAmplifier, AoeExclusionType... exclusions) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(
                center.getWorld(), center.getX(), center.getY(), center.getZ());
        areaEffectCloudEntity.setOwner(user);
        areaEffectCloudEntity.setRadius(cloudRadius);
        areaEffectCloudEntity.setRadiusOnUse((cloudRadius / 10) * -1);
        areaEffectCloudEntity.setWaitTime(cloudWaitTime);
        areaEffectCloudEntity.setDuration(cloudDuration);
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffect, effectDuration, effectAmplifier));
        ((IExclusiveAOECloud) areaEffectCloudEntity).mcdx$setExclusions(exclusions);
        center.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    //Exploding
    public static void spawnExplosionCloud(LivingEntity user, LivingEntity target, float radius) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(
                target.getWorld(), target.getX(), target.getY(), target.getZ());
        areaEffectCloudEntity.setOwner(user);
        areaEffectCloudEntity.setParticleType(ParticleTypes.EXPLOSION);
        areaEffectCloudEntity.setRadius(radius);
        areaEffectCloudEntity.setDuration(0);
        user.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    //Regen Arrow
    public static void spawnRegenCloudAtPos(LivingEntity user, boolean arrow, BlockPos blockPos, int amplifier, int duration) {
        int inGroundMitigator = arrow ? 1 : 0;
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(
                user.getWorld(), blockPos.getX(), blockPos.getY() + inGroundMitigator, blockPos.getZ());
        areaEffectCloudEntity.setOwner(user);
        areaEffectCloudEntity.setRadius(5.0F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setDuration(60);
        StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, duration, amplifier);
        areaEffectCloudEntity.addEffect(regeneration);
        user.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    public static void spawnStatusEffectCloud(LivingEntity owner, BlockPos blockPos, float radius, int duration, StatusEffectInstance... statusEffectInstances) {
        AreaEffectCloudEntity aoeCloudEntity = new AreaEffectCloudEntity(owner.getWorld(), blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        aoeCloudEntity.setOwner(owner);
        aoeCloudEntity.setRadius(radius);
        aoeCloudEntity.setRadiusOnUse(-0.5f);
        aoeCloudEntity.setWaitTime(10);
        aoeCloudEntity.setDuration(duration);
        for (StatusEffectInstance instance : statusEffectInstances)
            aoeCloudEntity.addEffect(instance);
        owner.getWorld().spawnEntity(aoeCloudEntity);
    }

    public static void spawnParticleCloud(LivingEntity attacker, LivingEntity victim, float radius, int duration, ParticleEffect particleEffect) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(victim.getWorld(), victim.getX(), victim.getY(), victim.getZ());
        areaEffectCloudEntity.setOwner(attacker);
        areaEffectCloudEntity.setRadius(radius);
        areaEffectCloudEntity.setDuration(duration);
        areaEffectCloudEntity.setParticleType(particleEffect);
        attacker.getWorld().spawnEntity(areaEffectCloudEntity);
    }

    public static class AoeBuilder {
        private float cloudRadius = 3f;
        private float cloudRadiusOnUse = 0f;
        private float cloudRadiusGrowth = 0f;
        private int cloudWaitTime = 20;
        private int cloudDuration = 600;
        ParticleEffect particle = null;
        private List<StatusHolder> statuses = new ArrayList<>();
        private Set<AoeExclusionType> exclusionTypes = new HashSet<>();

        public AoeBuilder radius(float cloudRadius) {
            this.cloudRadius = cloudRadius;
            return this;
        }

        public AoeBuilder radiusOnUse(float cloudRadiusOnUse) {
            this.cloudRadiusOnUse = cloudRadiusOnUse;
            return this;
        }

        public AoeBuilder radiusGrowth(float cloudRadiusGrowth) {
            this.cloudRadiusGrowth = cloudRadiusGrowth;
            return this;
        }

        public AoeBuilder waitTime(int cloudWaitTime) {
            this.cloudWaitTime = cloudWaitTime;
            return this;
        }

        public AoeBuilder duration(int cloudDuration) {
            this.cloudDuration = cloudDuration;
            return this;
        }


        public void build(LivingEntity user, Vec3d position) {
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(user.getWorld(), position.x, position.y, position.z);
            areaEffectCloudEntity.setOwner(user);
            areaEffectCloudEntity.setRadius(cloudRadius);
            areaEffectCloudEntity.setRadiusOnUse(cloudRadiusOnUse);
            areaEffectCloudEntity.setRadiusGrowth(cloudRadiusGrowth);
            areaEffectCloudEntity.setWaitTime(cloudWaitTime);
            areaEffectCloudEntity.setDuration(cloudDuration);
            if (particle != null) {
                areaEffectCloudEntity.setParticleType(particle);
            }
            if (!statuses.isEmpty()) {
                for (StatusHolder holder: statuses) {
                    areaEffectCloudEntity.addEffect(new StatusEffectInstance(holder.statusEffect, holder.duration, holder.amplifier));
                }
            }
            if (!exclusionTypes.isEmpty()) {
                ((IExclusiveAOECloud) areaEffectCloudEntity).mcdx$setExclusions(exclusionTypes);
            }
            user.getWorld().spawnEntity(areaEffectCloudEntity);
        }

        public void build(LivingEntity user, Vec3i position) {
            build(user, Vec3d.of(position));
        }

        public void build(LivingEntity user, LivingEntity target) {
            build(user, target.getPos());
        }

        private record StatusHolder(RegistryEntry<StatusEffect> statusEffect, int duration, int amplifier) {}
    }
}