package com.rainchat.villages.utilities.general;

import com.rainchat.villages.api.placeholder.PlaceholderSupply;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {

    private static final Pattern PH_KEY = Pattern.compile("%([\\w\\._-]+)%");

    private static final Pattern chatPattern = Pattern
            .compile("\\s*%(tellraw|title|subtitle|actionbar)%\\s*((?:(?!%(?:tellraw|title|subtitle|actionbar)%).)*)");


    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String format(String string) {
        return color(Message.PREFIX.toString() + string);
    }


    public static void sendTranslation(CommandSender p, boolean prefixed, String text, PlaceholderSupply<?>... replacementSource) {
        Player player = p instanceof Player ? (Player) p : null;
        text = translateRaw(text, player, prefixed, replacementSource);
        if (text.isEmpty())
            return;

        int tellrawPos = text.indexOf("%tellraw%");
        int titlePos = text.indexOf("%title%");
        int subtitlePos = text.indexOf("%subtitle%");
        int actionbarPos = text.indexOf("%actionbar%");
        if ((tellrawPos & titlePos & subtitlePos & actionbarPos) == -1) {
            p.sendMessage(new MineDown(text).toComponent());
            return;
        }


        int nb = -1 >>> 1;

        int matchStart = Math.min(Math.min(tellrawPos & nb, titlePos & nb),
                Math.min(subtitlePos & nb, actionbarPos & nb));

        if (matchStart > 0) {
            p.sendMessage(new MineDown(text.substring(0, matchStart)).toComponent());
        }

        if (player != null) {
            StringBuilder tellrawBuilder = new StringBuilder();
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder subtitleBuilder = new StringBuilder();
            StringBuilder actionbarBuilder = new StringBuilder();

            Matcher matcher = chatPattern.matcher(text.substring(matchStart));

            while (matcher.find()) {
                String type = matcher.group(1);
                String message = matcher.group(2);
                switch (type) {
                    case "tellraw":
                        tellrawBuilder.append(message);
                        break;
                    case "title":
                        titleBuilder.append(message);
                        break;
                    case "subtitle":
                        subtitleBuilder.append(message);
                        break;
                    case "actionbar":
                        actionbarBuilder.append(message);
                        break;

                    default:
                        break;
                }
            }

            String tellrawMessage = tellrawBuilder.toString();
            String titleMessage = titleBuilder.toString();
            String subtitleMessage = subtitleBuilder.toString();
            String actionbarMessage = actionbarBuilder.toString();

            if (!tellrawMessage.isEmpty())
                tellraw(player, tellrawMessage);

            if (!titleMessage.isEmpty() || !subtitleMessage.isEmpty()) {
                sendTitle(player, titleMessage, subtitleMessage, 10, 70, 20);
            }

            if (!actionbarMessage.isEmpty()) {
                sendActionbar(player, actionbarMessage);
            }
        }
    }

    public static void tellraw(Player p, String json) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tellraw " + p.getName() + " " + json);
    }


    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new MineDown(message).toComponent());
    }


    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }


    private static String translateRaw(String template, Player player, boolean prefixed, PlaceholderSupply<?>... replacementSource) {

        Matcher m = PH_KEY.matcher(template);
        while (m.find()) {

            for (PlaceholderSupply<?> e : replacementSource) {

                String replacement = e.getReplacement(m.group(1));
                if (replacement != null) {
                    if (!replacement.isEmpty()) {
                        template = template.replace(m.group(), replacement);
                        break;
                    }
                }
            }
        }
        if (!template.isEmpty() && prefixed)
            template = Message.PREFIX.toString() + template;

        return template;
    }
}
