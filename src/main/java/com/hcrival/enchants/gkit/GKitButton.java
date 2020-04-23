package com.hcrival.enchants.gkit;

import com.hcrival.enchants.Enchants;
import com.hcrival.enchants.util.ColorUtil;
import com.hcrival.enchants.util.ItemUtil;
import com.hcrival.enchants.util.menu.Button;
import com.hcrival.enchants.util.menu.Menu;
import net.frozenorb.qlib.util.TimeUtils;
import net.minecraft.server.v1_7_R4.ItemArmor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GKitButton extends Button {

    private Enchants instance = Enchants.getInstance();

    private String kitname;

    private String path;
    private String displayPath;

    public GKitButton(String path, String kitname) {
        this.path = path;
        this.displayPath = path + "Display.";
        this.kitname = kitname;
    }

    @Override
    public String getName(Player player) {
        return instance.getConfig().getString(displayPath + "Name");
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> toReturn = new ArrayList<>();

        GKitPlayer gKitPlayer = GKitPlayer.getPlayer(player);

        for (String lore : instance.getConfig().getStringList(displayPath + "Lore")) {
            if (gKitPlayer.canUseKit(kitname)) {
                lore = lore.replace("%cooldowwn%", "Now");
                lore = lore.replace("%cooldown%", "Now");
            } else {
                lore = lore.replace("%cooldowwn%", TimeUtils.formatIntoHHMMSS((int) (gKitPlayer.getKitCooldowns().get(kitname) - System.currentTimeMillis()) / 1000));
                lore = lore.replace("%cooldown%", TimeUtils.formatIntoHHMMSS((int) (gKitPlayer.getKitCooldowns().get(kitname) - System.currentTimeMillis()) / 1000));
            }

            toReturn.add(ColorUtil.format(lore));
        }

        return toReturn;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.getMaterial(Integer.parseInt(instance.getConfig().getString(displayPath + "Item")));
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new Menu() {
                @Override
                public String getTitle(Player player) {
                    return "Kit Preview";
                }

                @Override
                public Map<Integer, Button> getButtons(Player player) {
                    Map<Integer, Button> buttons = new HashMap<>();
                    Inventory inventory = new GKitInventoryBuilder(kitname, Enchants.getInstance().getConfig().getStringList(path + "Items")).buildInventory();

                    for (ItemStack is : inventory.getContents()) {
                        buttons.put(buttons.size(), new Button() {
                            @Override
                            public String getName(Player player) {
                                return "null";
                            }

                            @Override
                            public List<String> getDescription(Player player) {
                                return Collections.emptyList();
                            }

                            @Override
                            public Material getMaterial(Player player) {
                                return Material.REDSTONE;
                            }

                            @Override
                            public ItemStack getButtonItem(Player player) {
                                return is;
                            }
                        });
                    }

                    return buttons;
                }
            }.openMenu(player);
            return;
        }

        if (clickType == ClickType.LEFT) {
            if (player.hasPermission("crazyenchantments.gkitz." + kitname)) {
                GKitPlayer gKitPlayer = GKitPlayer.getPlayer(player);

                if (gKitPlayer.canUseKit(kitname)) {
                    Inventory inventory = new GKitInventoryBuilder(kitname, Enchants.getInstance().getConfig().getStringList(path + "Items")).buildInventory();
                    int armor = 0;
                    for (ItemStack item : inventory.getContents()) {
                        if (item == null) continue;
                        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor) {
                            if (ItemUtil.autoEquip(player, item)) {
                                armor++;
                                continue;
                            }
                        }

                        if (ItemUtil.getRealInventoryCount(player.getInventory()) >= player.getInventory().getSize()) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            continue;
                        }

                        player.getInventory().addItem(item);
                    }

                    if (armor > 0) {
                        for (int i = 0; i < armor; i++) {
                            if (ItemUtil.getRealInventoryCount(player.getInventory()) >= player.getInventory().getSize()) {
                                player.getWorld().dropItem(player.getLocation(), inventory.getContents()[ItemUtil.getRealInventoryCount(inventory) - 1]);
                            } else {
                                player.getInventory().addItem(inventory.getContents()[ItemUtil.getRealInventoryCount(inventory) - 1]);
                            }
                        }
                    }

                    player.closeInventory();

                    if (!player.hasPermission("qenchants.bypass")) {
                        GKitPlayer.getExecutorService().execute(() -> {
                            gKitPlayer.getKitCooldowns().put(kitname, System.currentTimeMillis() + (TimeUtils.parseTime(Enchants.getInstance().getConfig().getString(path + "Cooldown")) * 1000));
                            gKitPlayer.save();
                        });
                    }
                }
            }
        }
    }

}
