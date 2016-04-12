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
public class GOL extends gun.Sniper{
    public GOL() {
        closedamage = 99;
        longdamage = 9001;
        mpsec = 95;
        speed = mpsec / speedcoef;
        rpm = 30;
        coolinc = (double) 1200 / rpm;
        name = "GOL Magnum";
        clipsize = 6;
        left = clipsize;
        start = clipsize*4;
        ammo = start;
        recoilup = 40f;
        recoilside = 30f;
        reload = 90;
    }
    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(279));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone()  {
        GOL g = new GOL();
        g.holder = holder;
        return g;
    }
}
