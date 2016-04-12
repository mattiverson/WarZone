/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package special;

import gun.FullAuto;
import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class GAU17 extends gun.Shotgun implements Special, FullAuto{

    public GAU17() {
        closedamage = 1000;
        longdamage = 30;
        speed = 5.2D;
        coolinc = 0.5D;
        bullets = 4;
        spread = 0.99D;
        name = "GAU17";
        clipsize = 64*33;
        left = clipsize;
        start = left;
        ammo = left;
        reload = 10;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        Gun g = new GAU17();
        g.holder = holder;
        return g;
    }
}
