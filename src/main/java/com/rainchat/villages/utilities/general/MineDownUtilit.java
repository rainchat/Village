package com.rainchat.villages.utilities.general;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class MineDownUtilit {

    public static TextComponent conventMineDown(BaseComponent... baseComponents) {
        TextComponent textComponent = new TextComponent();
        for (BaseComponent baseComponent : baseComponents) {
            textComponent.addExtra(baseComponent);
        }
        return textComponent;
    }

}
