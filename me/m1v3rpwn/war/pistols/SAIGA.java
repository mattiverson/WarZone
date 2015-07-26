/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.m1v3rpwn.war.pistols;

import me.m1v3rpwn.war.gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class SAIGA extends me.m1v3rpwn.war.gun.Shotgun{
    
    public SAIGA() {
        bullets = 9;
        closedamage = 18;
        longdamage = 6;
        spread = 0.82D;
        mpsec = 30;
        speed = mpsec / speedcoef;
        rpm = 198;
        coolinc = (double) 1200 / rpm;
        name = "AWS";
        clipsize = 9;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 44;
        recoilup = 40f;
        recoilside = 30f;
        invslot = 1;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(277));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        SAIGA ret = new SAIGA();
        ret.holder = holder;
        return ret;
    }
    
}
