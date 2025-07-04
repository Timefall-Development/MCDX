package dev.timefall.mcdx.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;

public class ParticleHelper {

    private static void addParticles(ServerWorld world, LivingEntity nearbyEntity, ParticleEffect particleEffect) {

        double velX = 0;
        double velY = 1;
        double velZ = 0;

        double startX = nearbyEntity.getX() - .275f;
        double startY = nearbyEntity.getY();
        double startZ = nearbyEntity.getZ() - .275f;

        for (int i = 0; i < 10; i++) {
            double frontX = .5f * world.getRandom().nextDouble();
            world.spawnParticles(particleEffect, startX + frontX, startY + world.getRandom().nextDouble() * .5, startZ + .5f,
                    1,velX, velY, velZ, 0);

            double backX = .5f * world.getRandom().nextDouble();
            world.spawnParticles(particleEffect, startX + backX, startY + world.getRandom().nextDouble() * .5, startZ,1, velX, velY,
                    velZ,0);

            double leftZ = .5f * world.getRandom().nextDouble();
            world.spawnParticles(particleEffect, startX, startY + world.getRandom().nextDouble() * .5, startZ + leftZ,1, velX, velY,
                    velZ,0);

            double rightZ = .5f * world.getRandom().nextDouble();
            world.spawnParticles(particleEffect, startX + .5f, startY + world.getRandom().nextDouble() * .5, startZ + rightZ,1, velX,
                    velY, velZ,0);
        }
    }
}
