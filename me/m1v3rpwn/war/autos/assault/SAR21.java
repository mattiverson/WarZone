/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.autos.assault;

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
public class SAR21 extends Auto {

    public SAR21() {
        closedamage = 45;
        longdamage = 35;
        deacc = 0.92D;
        mpsec = 65;
        speed = mpsec / speedcoef;
        rpm = 600;
        coolinc = (double) 1200 / rpm;
        name = "SAR21";
        clipsize = 40;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 68;
        recoilup = 15f;
        recoilside = 30f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(263));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        SAR21 ret = new SAR21();
        ret.holder = holder;
        return ret;
    }
}
