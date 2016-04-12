package pistols;

import gun.FullAuto;
import gun.Gun;
import gun.Pistol;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class G18 extends Pistol implements FullAuto{

    public G18() {
        closedamage = 18;
        longdamage = 11;
        deacc = 0.75;
        speed = 3.8;
        rpm = 900;
        mpsec = 38;
        speed = mpsec / speedcoef;
        coolinc = (double) 1200 / rpm;
        name = "Glock 18";
        clipsize = 20;
        left = clipsize;
        start = clipsize*3;
        ammo = start;
        recoilup = 7f;
        recoilside = 14f;
        reload = 29;
    }

    @Override
    public ItemStack item() {
        ItemStack stack = new ItemStack(Material.getMaterial(273));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Gun clone() {
        G18 ret = new G18();
        ret.holder = holder;
        return ret;
    }
}
