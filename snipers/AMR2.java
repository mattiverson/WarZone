/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snipers;

import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author m1v3rpwn
 */
public class AMR2 extends gun.Sniper{
    public AMR2() {
        closedamage = 100;
        longdamage = 90;
        deacc = 1;
        mpsec = 88;
        speed = mpsec / speedcoef;
        rpm = 36;
        coolinc = (double) 1200 / rpm;
        name = "AMR-2";
        clipsize = 6;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 30f;
        recoilside = 20f;
        reload = 80;
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
    public Gun clone()  {
        Gun g = new AMR2();
        g.holder = holder;
        return g;
    }
}
