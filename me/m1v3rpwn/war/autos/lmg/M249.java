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
public class M249 extends Auto {

    public M249() {
        closedamage = 25;
        longdamage = 18;
        deacc = 0.92D;
        mpsec = 62;
        speed = mpsec / speedcoef;
        rpm = 848;
        coolinc = (double) 1200 / rpm;
        name = "M249";
        clipsize = 100;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 124;
        recoilup = 5f;
        recoilside = 10f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(265));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        M249 ret = new M249();
        ret.holder = holder;
        return ret;
    }
}
