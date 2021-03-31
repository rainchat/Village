package com.rainchat.villages.api.placeholder.replacer;

import com.rainchat.villages.api.placeholder.BaseReplacements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ArgsReplacements extends BaseReplacements<Player> {

    public List<String> args;


    public ArgsReplacements(String... strings) {
        super("args_");
        this.args = Arrays.asList(strings);
    }


    @Override
    public Class<Player> forClass() {
        return Player.class;
    }

    @Override
    public String getReplacement(String base, String fullKey) {

        if (isNumeric(base)){
            int number = Integer.parseInt(base);
            if (number <= args.size()){
                return args.get(number);
            }
        }

        return "";

    }

    public static boolean isNumeric(String strNum) {
        int d = 0;
        if (strNum == null) {
            return false;
        }
        try {
            d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}