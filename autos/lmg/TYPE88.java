/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autos.lmg;

import gun.Auto;
import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class TYPE88 extends Auto {

    public TYPE88() {
        closedamage = 40;
        longdamage = 30;
        deacc = 0.92D;
        mpsec = 60;
        speed = mpsec / speedcoef;
        rpm = 840;
        coolinc = (double) 1200 / rpm;
        name = "Type 88 LMG";
        clipsize = 200;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 140;
        recoilup = 5.5f;
        recoilside = 11f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(270));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        TYPE88 ret = new TYPE88();
        ret.holder = holder;
        return ret;
    }
}
