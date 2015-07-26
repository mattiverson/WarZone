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
public class L96A1 extends me.m1v3rpwn.war.gun.Sniper{
    public L96A1() {
        closedamage = 90;
        longdamage = 80;
        deacc = 0.98;
        mpsec = 82;
        speed = mpsec / speedcoef;
        rpm = 48;
        coolinc = (double) 1200 / rpm;
        name = "L96A1";
        clipsize = 18;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 0.08f;
        reload = 45;
    }
    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(280));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone()  {
        Gun g = new L96A1();
        g.holder = holder;
        return g;
    }
}
