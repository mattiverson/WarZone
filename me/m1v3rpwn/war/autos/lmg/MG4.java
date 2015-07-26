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
public class MG4 extends Auto {

    public MG4() {
        closedamage = 20;
        longdamage = 15;
        deacc = 0.92D;
        mpsec = 62;
        speed = mpsec / speedcoef;
        rpm = 1020;
        coolinc = (double) 1200 / rpm;
        name = "MG4";
        clipsize = 200;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 104;
        recoilup = 3.5f;
        recoilside = 7f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(266));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        MG4 ret = new MG4();
        ret.holder = holder;
        return ret;
    }
}
