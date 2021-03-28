package com.rainchat.villages.utilities.general;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.enums.ParticleTip;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleSpawn {

    public static void particleTusc(Player player, Chunk chunk, ParticleTip type) {
        if (type == ParticleTip.CLAIM) {

            new BukkitRunnable() {
                int timer = 3;

                @Override
                public void run() {
                    timer--;
                    claimParticle(chunk, player);
                    if (timer <= 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(Villages.getInstance(), 0, 20);

        } else if (type == ParticleTip.UN_CLAIM) {

            new BukkitRunnable() {
                int timer = 3;

                @Override
                public void run() {
                    timer--;
                    unClaimParticle(chunk, player);
                    if (timer <= 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(Villages.getInstance(), 0, 20);
        }


    }

    private static void claimParticle(Chunk chunk, Player player) {
        int minX = chunk.getX() * 16;
        int minZ = chunk.getZ() * 16;
        int minY = player.getLocation().getBlockY() + 2;
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 3);

        for (int x = minX; x < minX + 17; x++) {
            for (int z = minZ; z < minZ + 17; z++) {
                player.spawnParticle(Particle.REDSTONE, minX, minY, z, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, x, minY, minZ, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, minX + 16, minY, z, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, x, minY, minZ + 17, 1, dustOptions);
            }
        }
    }

    private static void unClaimParticle(Chunk chunk, Player player) {
        int minX = chunk.getX() * 16;
        int minZ = chunk.getZ() * 16;
        int minY = player.getLocation().getBlockY() + 2;
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 3);

        for (int x = minX; x < minX + 17; x++) {
            for (int z = minZ; z < minZ + 17; z++) {
                player.spawnParticle(Particle.REDSTONE, minX, minY, z, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, x, minY, minZ, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, minX + 16, minY, z, 1, dustOptions);
                player.spawnParticle(Particle.REDSTONE, x, minY, minZ + 17, 1, dustOptions);
            }
        }
    }

}
