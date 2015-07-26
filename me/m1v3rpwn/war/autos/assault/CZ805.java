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
public class CZ805 extends Auto {

    public CZ805() {
        closedamage = 37;
        longdamage = 27;
        deacc = 0.92D;
        mpsec = 58;
        speed = mpsec / speedcoef;
        rpm = 756;
        coolinc = (double) 1200 / rpm;
        name = "CZ805";
        clipsize = 30;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 58;
        recoilup = 18f;
        recoilside = 36f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(258));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        CZ805 ret = new CZ805();
        ret.holder = holder;
        return ret;
    }

}
