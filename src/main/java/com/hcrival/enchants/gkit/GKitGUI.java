package com.hcrival.enchants.gkit;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.util.ColorUtil;
import com.hcrival.enchants.util.menu.Button;
import com.hcrival.enchants.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GKitGUI extends Menu {

    private Enchants instance = Enchants.getInstance();

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (String key : instance.getConfig().getConfigurationSection("GKitz").getKeys(false)) {
            String path = "GKitz." + key + ".";
            buttons.put(Enchants.getInstance().getConfig().getInt(path + "Display.Slot") - 1, new GKitButton(path, key));
        }

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return ColorUtil.format(instance.getConfig().getString("Settings.Inventory-Name"));
    }

    @Override
    public int size(Player player) {
        return instance.getConfig().getInt("Settings.GUI-Size");
    }

}
