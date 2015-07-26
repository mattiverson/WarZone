/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.m1v3rpwn.war.snipers;

import me.m1v3rpwn.war.gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class SRR61 extends me.m1v3rpwn.war.gun.Sniper{
    public SRR61() {
        closedamage = 100;
        longdamage = 100;
        deacc = 0.98;
        mpsec = 94;
        speed = mpsec / speedcoef;
        rpm = 60;
        coolinc = (double) 1200 / rpm;
        name = "SRR-61";
        clipsize = 16;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 20f;
        recoilside = 30f;
        reload = 60;
    }
    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(281));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone()  {
        Gun g = new SRR61();
        g.holder = holder;
        return g;
    }
}
