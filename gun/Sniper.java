/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gun;

import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 *
 * @author m1v3rpwn
 */
public abstract class Sniper extends Gun implements Primary {

    public Sniper() {
        invslot = 0;
    }
    
    public Snowball[] fire() {
        if (ammo == 0) {
            holder.sendMessage("Out of ammo!");
            return new Snowball[0];
        }
        
        final Snowball a = holder.launchProjectile(org.bukkit.entity.Snowball.class);
        
        Vector vel = a.getVelocity();
        if (!scoped) {
            vel.setY(vel.getY() * (Math.random() * 0.6 + 0.7));
            vel.setX(vel.getX() * (Math.random() * 0.6 + 0.7));
            vel.setZ(vel.getZ() * (Math.random() * 0.6 + 0.7));
        }
        double coef = speed / vel.length();
        vel.multiply(coef);
        a.setVelocity(vel);
        
        cooldown += coolinc;
        
        recoil();
        
        ammo--;
        left--;
        
        updateName();
        
        holder.updateInventory();
        
        return new Snowball[]{a};
    }
}
