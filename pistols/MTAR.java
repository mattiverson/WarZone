package pistols;

import gun.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MTAR extends gun.Pistol implements gun.FullAuto{

    public MTAR() {
        closedamage = 20;
        longdamage = 15;
        deacc = 0.75;
        mpsec = 54;
        speed = mpsec / speedcoef;
        rpm = 1080;
        coolinc = (double) 1200 / rpm;
        name = "MTAR-21";
        clipsize = 20;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 9f;
        recoilside = 18f;
        reload = 60;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(274));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        MTAR ret = new MTAR();
        ret.holder = holder;
        return ret;
    }
}
