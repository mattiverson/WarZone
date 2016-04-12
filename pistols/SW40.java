package pistols;

import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SW40 extends gun.Pistol {

    public SW40() {
        closedamage = 56;
        longdamage = 28;
        deacc = 0.82;
        mpsec = 40;
        speed = mpsec / speedcoef;
        rpm = 126;
        coolinc = (double) 1200 / rpm;
        name = "SW40";
        clipsize = 6;
        left = clipsize;
        start = clipsize*5;
        ammo = start;
        recoilup = 20f;
        recoilside = 15f;
        reload = 76;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(275));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        SW40 ret = new SW40();
        ret.holder = holder;
        return ret;
    }
}
