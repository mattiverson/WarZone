/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.m1v3rpwn.war.gun;

import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author miverson
 */
public abstract class Shotgun extends Gun {

    public int bullets;
    public double spread;

    @Override
    public Snowball[] fire() {
        if (ammo == 0) {
            holder.sendMessage("Out of ammo!");
            return new Snowball[0];
        }
        
        Snowball[] ret = new Snowball[bullets];
        
        for (int i = 0; i < bullets; i++) {
            Snowball a = holder.launchProjectile(org.bukkit.entity.Snowball.class);
            
            Vector vel = a.getVelocity();
            double coef = speed / vel.length();
            vel.multiply(coef);
            double diff = 1 + Math.random() * (1 - spread) - (1 - spread) / 2;
            vel.setX(vel.getX() * diff);
            diff = 1 + Math.random() * (1 - spread) - (1 - spread) / 2;
            vel.setY(vel.getY() * diff);
            diff = 1 + Math.random() * (1 - spread) - (1 - spread) / 2;
            vel.setZ(vel.getZ() * diff);
            a.setVelocity(vel);
            
            ret[i] = a;
        }
        cooldown += coolinc;
        
        recoil();
        
        ammo--;
        left--;
        
        updateName();
        
        holder.updateInventory();
        
        return ret;
    }
}
