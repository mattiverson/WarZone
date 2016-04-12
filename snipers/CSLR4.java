/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snipers;

import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class CSLR4 extends gun.Sniper{
    public CSLR4() {
        closedamage = 75;
        longdamage = 60;
        deacc = 1;
        mpsec = 91;
        rpm = 48;
        speed = mpsec / speedcoef;
        coolinc = (double) 1200 / rpm;
        name = "CS-LR4";
        clipsize = 10;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 20f;
        recoilside = 10;
        reload = 40;
    }
    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(278));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone()  {
        Gun g = new CSLR4();
        g.holder = holder;
        return g;
    }
}
