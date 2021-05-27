package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillagePlayer;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CuboidEvent implements Listener {

    VillageManager villageManager;


    public CuboidEvent(VillageManager villageManager) {
        this.villageManager = villageManager;
    }

    @EventHandler
    public void onClickStick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType().equals(Material.STICK)) {
            event.setCancelled(true);
            Action action = event.getAction();
            VillagePlayer villagePlayer = villageManager.getVillagePlayer(player);
            if (action == Action.LEFT_CLICK_BLOCK) {
                villagePlayer.setPos1(event.getClickedBlock().getLocation());
                player.sendMessage(Chat.translateRaw("&7pos1: &e"
                        + villagePlayer.getPos1().getBlockX() + " "
                        + villagePlayer.getPos1().getBlockY() + " "
                        + villagePlayer.getPos1().getBlockZ()));
            }

            if (action == Action.RIGHT_CLICK_BLOCK) {
                villagePlayer.setPos2(event.getClickedBlock().getLocation());
                player.sendMessage(Chat.translateRaw("&7pos2: &e"
                        + villagePlayer.getPos2().getBlockX() + " "
                        + villagePlayer.getPos2().getBlockY() + " "
                        + villagePlayer.getPos2().getBlockZ()));
            }
        }
    }

    @EventHandler
    public void onTrample(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        final Block soil = event.getClickedBlock();
        if (soil == null || soil.getType() != Material.FARMLAND) {
            return;
        }

        Village currentVillage = villageManager.getVillage(event.getPlayer().getLocation().getChunk());

        if (currentVillage != null) {
            if (!currentVillage.hasMember(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

}
