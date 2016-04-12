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
public class PKP extends Auto {

    public PKP() {
        closedamage = 40;
        longdamage = 30;
        deacc = 0.92D;
        mpsec = 56;
        speed = mpsec / speedcoef;
        rpm = 780;
        coolinc = (double) 1200 / rpm;
        name = "PKP";
        clipsize = 150;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 110;
        recoilup = 10f;
        recoilside = 20f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(269));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        PKP ret = new PKP();
        ret.holder = holder;
        return ret;
    }
}
