/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import special.RayCaster;
import special.GAU17;
import snipers.L96A1;
import snipers.CSLR4;
import snipers.GOL;
import snipers.SRR61;
import pistols.SW40;
import pistols.FN57;
import pistols.MTAR;
import pistols.SAIGA;
import pistols.G18;
import autos.lmg.AWS;
import autos.lmg.MG4;
import autos.lmg.PKP;
import autos.lmg.M249;
import autos.lmg.TYPE88;
import autos.assault.FAMAS;
import autos.assault.SAR21;
import autos.assault.CZ805;
import autos.assault.AK12;
import autos.assault.ACE23;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import gun.Gun;
import main.Main.Loadout;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 *
 * @author m1v3rpwn
 */
public class Util {

    public final HashMap<String, Gun> gunlist = new HashMap<>();

    public final ItemStack INFECT_SWORD = new ItemStack(Material.DIAMOND_SWORD);

    public final ItemStack[] RED_ARMOR = new ItemStack[4];

    public final ItemStack[] BLUE_ARMOR = new ItemStack[4];

    public final ItemStack loadEdit = new ItemStack(Material.BOWL);

    public final HashMap<String, Integer> unlocks = new HashMap<>();

    public final HashMap<String, Integer> prices = new HashMap<>();

    private Main main;

    public Util(Main m) {
        main = m;

        gunlist.put("ACE23", new ACE23());
        gunlist.put("AK12", new AK12());
        gunlist.put("CZ805", new CZ805());
        gunlist.put("FAMAS", new FAMAS());
        gunlist.put("SAR21", new SAR21());
        gunlist.put("AWS", new AWS());
        gunlist.put("M249", new M249());
        gunlist.put("MG4", new MG4());
        gunlist.put("PKP", new PKP());
        gunlist.put("TYPE88LMG", new TYPE88());
        gunlist.put("FN57", new FN57());
        gunlist.put("GLOCK18", new G18());
        gunlist.put("MTAR", new MTAR());
        gunlist.put("SAIGA", new SAIGA());
        gunlist.put("SW40", new SW40());
        gunlist.put("CSLR4", new CSLR4());
        gunlist.put("GOLMAGNUM", new GOL());
        gunlist.put("L96A1", new L96A1());
        gunlist.put("SRR61", new SRR61());
        gunlist.put("GAU17", new GAU17());
        gunlist.put("RAYCASTER", new RayCaster());

        INFECT_SWORD.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 100);
        INFECT_SWORD.getItemMeta().setDisplayName(ChatColor.DARK_RED + "Infected Sword");

        ItemStack is = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
        lam.setColor(Color.RED);
        is.setItemMeta(lam);
        RED_ARMOR[0] = is;
        is = new ItemStack(Material.LEATHER_LEGGINGS);
        is.setItemMeta(lam);
        RED_ARMOR[1] = is;
        is = new ItemStack(Material.LEATHER_CHESTPLATE);
        is.setItemMeta(lam);
        RED_ARMOR[2] = is;
        is = new ItemStack(Material.LEATHER_HELMET);
        is.setItemMeta(lam);
        RED_ARMOR[3] = is;

        is = new ItemStack(Material.LEATHER_BOOTS);
        lam.setColor(Color.BLUE);
        is.setItemMeta(lam);
        BLUE_ARMOR[0] = is;
        is = new ItemStack(Material.LEATHER_LEGGINGS);
        is.setItemMeta(lam);
        BLUE_ARMOR[1] = is;
        is = new ItemStack(Material.LEATHER_CHESTPLATE);
        is.setItemMeta(lam);
        BLUE_ARMOR[2] = is;
        is = new ItemStack(Material.LEATHER_HELMET);
        is.setItemMeta(lam);
        BLUE_ARMOR[3] = is;

        ItemMeta meta = loadEdit.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Loadout Editor");
        loadEdit.setItemMeta(meta);

        unlocks.put("SW40", 6);//pistol2
        prices.put("SW40", 75);
        unlocks.put("ACE23", 6);//assault2
        prices.put("ACE23", 100);
        unlocks.put("M249", 6);//LMG2
        prices.put("M249", 100);
        unlocks.put("AMR2", 6);//sniper2
        prices.put("AMR2", 100);
        unlocks.put("SAIGA", 11);//pistol3
        prices.put("SAIGA", 200);
        unlocks.put("CZ805", 11);//assault3
        prices.put("CZ805", 250);
        unlocks.put("MG4", 11);//LMG3
        prices.put("MG4", 250);
        unlocks.put("GOLMAGNUM", 11);//sniper3
        prices.put("GOLMAGNUM", 250);
        unlocks.put("G18", 18);//pistol4
        prices.put("G18", 450);
        unlocks.put("FAMAS", 18);//assault4
        prices.put("FAMAS", 600);
        unlocks.put("PKP", 18);//LMG4
        prices.put("PKP", 600);
        unlocks.put("L96A1", 18);//sniper4
        prices.put("L96A1", 600);
        unlocks.put("MTAR", 25);//pistol5
        prices.put("MTAR", 700);
        unlocks.put("SAR21", 25);//assault5
        prices.put("SAR21", 900);
        unlocks.put("TYPE88LMG", 25);//LMG5
        prices.put("TYPE88LMG", 900);
        unlocks.put("SRR61", 25);//sniper5
        prices.put("SRR61", 900);
        unlocks.put("RAYCASTER", 1);//this thing
        prices.put("RAYCASTER", 1);
//        price progression: 100,250,600,900,1500
//        pistols are only ~75% of the normal price
    }

    public boolean isRed(Player p) {
        if (main.red.contains(p)) {
            return true;
        }
        return false;
    }

    public ChatColor teamColor(Player p) {
        return isRed(p) ? ChatColor.RED : ChatColor.BLUE;
    }
    
    public ArrayList<Gun> getGuns(List<String> input) {
            ArrayList<Gun> ret = new ArrayList<>();
            for (String s : input) {
                ret.add(Main.me.u.gunlist.get(s).clone());
            }
            return ret;
        }
    
    public static Loadout parse(String input) {
        Loadout ret = Main.me.new Loadout();
        String[] dat = input.split("-");
        ret.primary = Main.me.u.gunlist.get(dat[0]).clone();
        for (int i = 0; i < 3; i++) {
        }
        ret.secondary = Main.me.u.gunlist.get(dat[1]).clone();
        for (int i = 0; i < 3; i++) {
        }
        return ret;
    }
}
