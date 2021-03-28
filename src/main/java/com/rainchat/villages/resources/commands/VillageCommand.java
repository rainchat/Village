package com.rainchat.villages.resources.commands;


import com.rainchat.villages.Villages;
import com.rainchat.villages.managers.ConfigSettings;
import com.rainchat.villages.resources.commands.subcommands.HelpCommand;
import com.rainchat.villages.utilities.general.Chat;
import com.rainchat.villages.utilities.general.Checks;
import com.rainchat.villages.utilities.general.Command;
import com.rainchat.villages.utilities.general.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class VillageCommand extends Command implements TabCompleter {

    private final Villages villages;
    private Set<Command> commands;

    public VillageCommand(Villages villages) {
        super("village", "");
        this.villages = villages;
        this.commands = new HashSet<>();
    }

    public boolean run(Player player, String[] args) throws IOException {
        List list = ConfigSettings.CLAIM_ENABLED_WORLDS_LIST.getList();
        List<String> enabledWorlds = new ArrayList<>();

        for (Object object : list) {
            String string = (String) object;
            if (Checks.checkWorld(string)) enabledWorlds.add(string);
        }

        if (enabledWorlds.stream().anyMatch(s -> s.equals(player.getWorld().getName()))) {
            if (args.length > 0) {
                for (Command command : commands) {
                    if (args[0].equalsIgnoreCase(command.toString())) {
                        if (player.hasPermission("village.command." + command.toString())) {
                            command.run(player, args);
                        } else {
                            player.sendMessage(Chat.format(Message.NO_COMMAND_PERMISSION.toString()));
                        }
                        break;
                    }
                }
            } else {
                if (player.hasPermission("village.command." + "help")) {
                    return new HelpCommand(this).run(player, args);
                } else {
                    player.sendMessage(Chat.format(Message.NO_COMMAND_PERMISSION.toString()));
                }
            }
        } else {
            player.sendMessage(Chat.format(Message.WORLD_NOT_ENABLED.toString()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> tabRun(Player player, String[] args) {
        return null;
    }

    public void initialise(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commands);
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {

        List<String> list = new ArrayList<>();
        if (!(commandSender instanceof Player)) {
            return null;
        }
        Player player = (Player) commandSender;
        if (strings.length == 1) {
            for (Command subCommand : commands) {
                if (commandSender.hasPermission(toString() + ".command." + subCommand.toString())) {
                    list.add(subCommand.toString());
                }
            }
        } else {
            for (Command subCommand : commands) {
                if (subCommand.toString().equalsIgnoreCase(strings[0])) {
                    return subCommand.tabRun(player, strings);
                }
            }
        }
        return list;
    }
}
