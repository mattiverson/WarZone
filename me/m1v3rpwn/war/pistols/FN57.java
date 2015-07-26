package me.m1v3rpwn.war.pistols;

import me.m1v3rpwn.war.gun.Gun;
import me.m1v3rpwn.war.gun.Pistol;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FN57 extends Pistol {

    public FN57() {
        closedamage = 22;
        longdamage = 12;
        deacc = 0.7;
        speed = 2.8;
        rpm = 450;
        mpsec = 43;
        speed = mpsec / speedcoef;
        coolinc = (double) 1200 / rpm;
        name = "FN57";
        clipsize = 20;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 6f;
        recoilside = 12f;
        reload = 28;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(271));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        FN57 ret = new FN57();
        ret.holder = holder;
        return ret;
    }
}
