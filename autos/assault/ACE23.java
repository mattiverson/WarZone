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
public class ACE23 extends Auto {

    public ACE23() {
        closedamage = 25;
        longdamage = 18;
        deacc = 0.8D;
        mpsec = 62;
        speed = mpsec / speedcoef;
        rpm = 828;
        coolinc = (double) 1200 / rpm;
        name = "ACE 23";
        clipsize = 25;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 48;
        recoilup = 10f;
        recoilside = 20f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(256));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        ACE23 ret = new ACE23();
        ret.holder = holder;
        return ret;
    }

}
