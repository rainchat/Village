package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.api.events.PlayerChangeChunkEvent;
import com.rainchat.villages.api.events.PlayerChangeLandEvent;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.data.village.VillagePlayer;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    private final VillageManager villageManager;

    public MoveEvent(VillageManager villageManager) {
        this.villageManager = villageManager;
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Chunk oldC = event.getFrom().getChunk();
        Chunk newC = event.getTo().getChunk();
        if (!oldC.equals(newC)) {
            PlayerChangeChunkEvent chunkEvent = new PlayerChangeChunkEvent(event.getPlayer(), oldC, newC, event.getFrom(), event.getTo());
            Bukkit.getServer().getPluginManager().callEvent(chunkEvent);
            if (chunkEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangeChunk(PlayerChangeChunkEvent event) {
        Player player = event.getPlayer();

        VillagePlayer landPlayer = villageManager.getVillagePlayer(player);
        Village fromLand = landPlayer.getCurrentVillage();

        Chunk toChunk = event.getToChunk();
        Village toLand = villageManager.getVillage(toChunk);

        if (toLand != fromLand) {
            PlayerChangeLandEvent landEvent = new PlayerChangeLandEvent(player, fromLand, toLand);
            Bukkit.getServer().getPluginManager().callEvent(landEvent);

            if (landEvent.isCancelled()) {
                event.setCancelled(true);
            } else {
                landPlayer.setCurrentLand(toLand);
                if (toLand != null) {
                    player.sendTitle(
                            Chat.color(Message.TITLE_HEADER.toString().replace("{0}", toLand.getName())),
                            Chat.color("&7" + toLand.getDescription()),
                            10, 40, 10
                    );
                } else {
                    player.sendTitle(
                            Chat.color(Message.TITLE_WILDERNESS_HEADER.toString()),
                            Chat.color(Message.TITLE_WILDERNESS_FOOTER.toString()),
                            10, 30, 10
                    );
                }
            }
        }
    }


}
