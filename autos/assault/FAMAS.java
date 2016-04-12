/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autos.assault;

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
public class FAMAS extends Auto {

    public FAMAS() {
        longdamage = 18;
        closedamage = 25;
        deacc = 0.8D;
        mpsec = 63;
        speed = mpsec / speedcoef;
        rpm = 960;
        coolinc = (double) 1200 / rpm;
        name = "FAMAS";
        clipsize = 20;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 50;
        recoilup = 5f;
        recoilside = 10f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(262));
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
