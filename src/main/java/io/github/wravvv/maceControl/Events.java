package io.github.wravvv.maceControl;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;


public class Events implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {

            Player attacker = (Player) event.getDamager();
            ItemStack heldItem = attacker.getInventory().getItemInMainHand();

            if (heldItem.getType().equals(Material.MACE)) {

                if (MaceControl.config.getBoolean("mace-disabled")) {
                    disabled(event,MaceControl.config.getString("feedback-msg-disabled"));
                } else {
                    int cooldown = MaceControl.config.getInt("mace-cooldown");
                    if (cooldown > 0) {
                        if (attacker.getCooldown(Material.MACE) > 0) {
                            disabled(event,MaceControl.config.getString("feedback-msg-cooldown"));
                        } else {
                            attacker.setCooldown(Material.MACE,cooldown*20);
                        }
                    }
                }

            }
        }
    }


    void disabled(EntityDamageByEntityEvent event, String message) {
        String formattedMessage = message.replace('&','§');
        int maxDamage = MaceControl.config.getInt("max-damage");
        String cancelWhen = MaceControl.config.getString("cancel-when").toLowerCase();
        if ((cancelWhen.equals("above max") && event.getDamage() > maxDamage) || cancelWhen.equals("always")) {
            if (MaceControl.config.getString("cancel-method").equalsIgnoreCase("cancel")) {
                event.setCancelled(true);
            } else {
                event.setDamage(Math.clamp(event.getDamage(),0,maxDamage));
            }
            feedback((Player) event.getDamager(),formattedMessage);
        }
    }

    void feedback(Player player, String message) {
        String feedbackType = MaceControl.config.getString("feedback");
        if (feedbackType.equalsIgnoreCase("both") || feedbackType.equalsIgnoreCase("chat")) {
            player.sendMessage(message);
        }
        if (feedbackType.equalsIgnoreCase("both") || feedbackType.equalsIgnoreCase("action bar")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
        }
    }
}
