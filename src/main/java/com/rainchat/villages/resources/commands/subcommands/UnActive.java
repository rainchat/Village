package com.rainchat.villages.resources.commands.subcommands;

import com.rainchat.villages.managers.VillageManager;
import com.rainchat.villages.utilities.general.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class UnActive extends Command {

    private final VillageManager villageManager;

    public UnActive(VillageManager villageManager) {
        super("unactive", "unactive [player]");
        this.villageManager = villageManager;
    }

    @Override
    public boolean run(Player player, String[] args) {

        villageManager.deleteNonActive();

        return true;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }

}
