package com.rainchat.villages.resources.listeners;

import com.rainchat.villages.Villages;
import com.rainchat.villages.data.village.Village;
import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class MoveEvent implements Listener {

    private final VillageManager villageManager;

    public MoveEvent(Villages villages) {
        this.villageManager = villages.getVillageManager();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {
            Village from = villageManager.getVillage(event.getFrom().getChunk());
            Village to = villageManager.getVillage(event.getTo().getChunk());

            if (villageManager.getVillage(event.getFrom().getChunk()) == villageManager.getVillage(event.getTo().getChunk()))
                return;

            if (from == null && to != null) {
                event.getPlayer().sendTitle(
                        Chat.color(Message.TITLE_HEADER.toString().replace("{0}", to.getName())),
                        Chat.color("&7" + to.getDescription()),
                        10, 30, 10
                );
                return;
            }

            if (to == null && from != null) {
                event.getPlayer().sendTitle(
                        Chat.color(Message.TITLE_WILDERNESS_HEADER.toString()),
                        Chat.color(Message.TITLE_WILDERNESS_FOOTER.toString()),
                        10, 30, 10
                );
            }

            if (to != null && to != from) {
                event.getPlayer().sendTitle(
                        Chat.color(Message.TITLE_HEADER.toString().replace("{0}", to.getName())),
                        Chat.color("&7" + to.getDescription()),
                        10, 30, 10
                );
            }
        }
    }


}
