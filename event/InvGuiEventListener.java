/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import gun.Gun;
import gun.Primary;
import gun.Secondary;
import java.util.ArrayList;
import java.util.List;
import main.Main;
import main.Main.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class InvGuiEventListener implements org.bukkit.event.Listener {

    Main m;
    InvGuiBuilder build;

    public InvGuiEventListener(Main main) {
        m = main;
        build = new InvGuiBuilder(main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void select(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (!m.players.contains(p)) {
            return;
        }
        switch (e.getInventory().getTitle().substring(2)) {
            case "Loadout":
                switch (e.getCurrentItem().getItemMeta().getDisplayName().substring(2)) {
                    case "Primary":
                        p.openInventory(build.buildPrim(p));
                        break;
                    case "Secondary":
                        p.openInventory(build.buildSec(p));
                        break;
                    case "Buy new guns":
                        p.openInventory(build.buildBuy(p));
                        break;
                }
                break;
            case "Primary":
                p.performCommand("prim " + e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().substring(2).replaceAll("-", "").replaceAll(" ", ""));
                p.closeInventory();
                break;
            case "Secondary":
                p.performCommand("sec " + e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().substring(2).replaceAll("-", "").replaceAll(" ", ""));
                p.closeInventory();
                break;
            case "Buy new guns":
                p.performCommand("buy " + e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().substring(2).replaceAll("-", "").replaceAll(" ", ""));
                p.closeInventory();
                break;
        }
        if (!p.getGameMode().equals(GameMode.CREATIVE)) {
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType().equals(Material.BOWL) && m.players.contains(e.getPlayer())) {
            e.getPlayer().openInventory(build.getMain());
        }
    }
}

class InvGuiBuilder {

    Main m;

    public InvGuiBuilder(Main main) {
        m = main;
    }

    public Inventory buildPrim(Player p) {
        Inventory ret = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Primary");
        PlayerData dat = m.datByName(p.getName());
        for (String g : dat.available) {
            if (Main.me.u.gunlist.get(g) instanceof Primary) {
                ret.addItem(Main.me.u.gunlist.get(g).item());
            }
        }
        return ret;
    }

    public Inventory buildSec(Player p) {
        Inventory ret = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Secondary");
        PlayerData dat = m.datByName(p.getName());
        for (String g : dat.available) {
            if (Main.me.u.gunlist.get(g) instanceof Secondary) {
                ret.addItem(Main.me.u.gunlist.get(g).item());
            }
        }
        return ret;
    }

    public Inventory buildBuy(Player p) {
        Inventory ret = Bukkit.createInventory(null, 27, ChatColor.RED + "Buy new guns");
        PlayerData dat = m.datByName(p.getName());
        for (String g : dat.buyable) {
            Bukkit.broadcastMessage(g);
            ItemStack add = Main.me.u.gunlist.get(g).item();
            List<String> lore = new ArrayList<>();
            if (Main.me.u.gunlist.get(g) instanceof Primary) {
                lore.add(ChatColor.GREEN + "Primary");
            } else {
                lore.add(ChatColor.BLUE + "Secondary");
            }
            ItemMeta meta = add.getItemMeta();
            meta.setLore(lore);
            add.setItemMeta(meta);
            ret.addItem(add);
        }
        return ret;
    }

    Inventory main;

    {
        main = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Loadout");
        ItemStack name = new ItemStack(Material.PAPER);
        ItemMeta nameMeta = name.getItemMeta();
        nameMeta.setDisplayName(ChatColor.AQUA + "Primary");
        name.setItemMeta(nameMeta);
        main.addItem(name);
        name = new ItemStack(Material.PAPER);
        nameMeta.setDisplayName(ChatColor.GREEN + "Secondary");
        name.setItemMeta(nameMeta);
        main.addItem(name);
        name = new ItemStack(Material.PAPER);
        nameMeta.setDisplayName(ChatColor.RED + "Buy new guns");
        name.setItemMeta(nameMeta);
        main.addItem(name);
    }

    public Inventory getMain() {
        return main;
    }
}
