/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.autos.lmg;

import me.m1v3rpwn.war.gun.Auto;
import me.m1v3rpwn.war.gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class AWS extends Auto {

    public AWS() {
        closedamage = 25;
        longdamage = 18;
        deacc = 0.92D;
        mpsec = 58;
        speed = mpsec / speedcoef;
        rpm = 780;
        coolinc = (double) 1200 / rpm;
        name = "AWS";
        clipsize = 75;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 57;
        recoilup = 8f;
        recoilside = 16f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(264));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        AWS ret = new AWS();
        ret.holder = holder;
        return ret;
    }
}
