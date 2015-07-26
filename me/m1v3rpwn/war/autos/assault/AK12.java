package me.m1v3rpwn.war.autos.assault;

import me.m1v3rpwn.war.gun.Auto;
import me.m1v3rpwn.war.gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AK12 extends Auto {

    public AK12() {
        longdamage = 18;
        closedamage = 25;
        deacc = 0.8D;
        mpsec = 60;
        speed = mpsec / speedcoef;
        rpm = 648;
        coolinc = (double) 1200 / rpm;
        name = "AK-12";
        clipsize = 30;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        reload = 60;
        recoilup = 10f;
        recoilside = 20f;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(257));
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
