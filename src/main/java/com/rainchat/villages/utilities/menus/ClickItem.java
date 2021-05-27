package com.rainchat.villages.utilities.menus;

import com.rainchat.villages.utilities.general.Item;

import java.util.List;

public class ClickItem extends Item {

    int slot = 0;
    List<String> commands;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
