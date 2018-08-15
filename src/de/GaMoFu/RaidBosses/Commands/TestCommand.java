package de.GaMoFu.RaidBosses.Commands;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Stray;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.Dungeon;
import de.GaMoFu.RaidBosses.PlayerSettings;
import de.GaMoFu.RaidBosses.PlayerSettings.MonsterSelection;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Monsters.Zombie;
import de.GaMoFu.RaidBosses.Monsters.Zomboss;
import de.GaMoFu.RaidBosses.ParticleEffects.RingEffect;
import de.GaMoFu.RaidBosses.Trader.Trader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R1.ChatClickable;
import net.minecraft.server.v1_13_R1.ChatClickable.EnumClickAction;
import net.minecraft.server.v1_13_R1.ChatHoverable;
import net.minecraft.server.v1_13_R1.ChatHoverable.EnumHoverAction;
import net.minecraft.server.v1_13_R1.ChatMessage;
import net.minecraft.server.v1_13_R1.ChatMessageType;
import net.minecraft.server.v1_13_R1.ChatModifier;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;

public class TestCommand implements ICommandHandler {

    @Override
    public boolean handleCommand(RaidBosses plugin, CommandSender sender, Command command, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 2 && args[0].equalsIgnoreCase("inst")) {

            plugin.getInstances().createNewInstance(player.getWorld(), args[1]);
            return true;

        } else if (args.length == 1 && args[0].equalsIgnoreCase("monster")) {
            Optional<Dungeon> instance = plugin.getInstances().getFromWorld(player.getWorld());
            if (!instance.isPresent()) {
                player.sendMessage("Instance not found for world '" + ChatColor.BLUE + player.getWorld().getName()
                        + ChatColor.RESET + "'");
            }
            instance.get().addMonster(Zombie.ALIAS, player.getLocation());
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("boss")) {
            Optional<Dungeon> instance = plugin.getInstances().getFromWorld(player.getWorld());
            if (!instance.isPresent()) {
                player.sendMessage("Instance not found for world '" + ChatColor.BLUE + player.getWorld().getName()
                        + ChatColor.RESET + "'");
            }
            instance.get().addBoss(Zomboss.ALIAS, player.getLocation());
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("inf")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("add")) {
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10);
                player.addPotionEffect(effect);

                player.sendMessage("Buffed with insane Regeneration!");
            } else {
                player.removePotionEffect(PotionEffectType.REGENERATION);

                player.sendMessage("Removed Regeneration");
            }
            return true;
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("merchant")) {
            Trader t = plugin.getTraderFactory().getTrader(args[1]);
            if (t == null) {
                player.sendMessage("Trader: " + args[1] + " not found.");
                return true;
            }

            t.spawn(player.getLocation());
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("holo")) {
            plugin.getHologramHandler().showHologram(player.getEyeLocation(), args[1], 20 * 5, player);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("falling")) {
            int amount = Integer.parseInt(args[1]);
            float radius = Float.parseFloat(args[2]);

            plugin.getFallingSwordHandler().spawnInArea(player.getLocation().add(0, 5, 0), radius, amount,
                    Material.DIAMOND_SWORD, player, 10, 3);
            // double x = Math.PI*Double.parseDouble(args[1])/180.0;
            // double y = Math.PI*Double.parseDouble(args[2])/180.0;
            // double z = Math.PI*Double.parseDouble(args[3])/180.0;
            //
            // ArmorStand a = (ArmorStand)
            // player.getWorld().spawnEntity(player.getLocation().add(0, 10, 0),
            // EntityType.ARMOR_STAND);
            // a.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            //// a.setVisible(false);
            // a.setRightArmPose(new EulerAngle(x, y, z));
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("ring")) {

            PlayerSettings ps = plugin.getPlayerSettings(player);
            MonsterSelection sel = ps.getMonsterSelection();
            final Location loc;
            if (sel != null) {
                loc = sel.getMonster().getMonsterEntity().getEntity().getLocation();
            } else {
                loc = player.getLocation();
            }

            int delay = Integer.parseInt(args[1]);
            player.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 0.5f);
            for (int i = 1; i <= 8; i++) {
                final int radius = i;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        RingEffect.doEffect(loc, Particle.WATER_SPLASH, 100, 0.5f, 0.5f, 0, 0, 20, radius);
                        RingEffect.doEffect(loc, Particle.SNOWBALL, 20, 0.5f, 0.5f, 0, 0, 20, radius);
                    }

                }, delay * i);
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("bear")) {
            Stray stray = (Stray) player.getWorld().spawnEntity(player.getLocation().add(1, 0, 1), EntityType.STRAY);

            PolarBear bear = (PolarBear) player.getWorld().spawnEntity(player.getLocation().add(0, 0, 1),
                    EntityType.POLAR_BEAR);
            bear.addPassenger(stray);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("phantom")) {
            Phantom p = (Phantom) player.getWorld().spawnEntity(player.getLocation().add(1, 0, 1), EntityType.PHANTOM);
            p.setSize(Integer.parseInt(args[1]));
            return true;
        }else if(args.length==2 &&args[0].equals("actionbar")) {
            
            BaseComponent[] specialText = new ComponentBuilder(args[1]).color(net.md_5.bungee.api.ChatColor.GREEN).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("hover text").color(net.md_5.bungee.api.ChatColor.GOLD).create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "you clicked on it")).create();
            
            TextComponent all1 =   new TextComponent(new TextComponent("Click here ["),specialText[0],new TextComponent("]"));
            
            player.spigot().sendMessage(all1);
            
            
            
            TextComponent part1 = new TextComponent("Click here [");
            
            TextComponent  part2 = new TextComponent(args[1]);
            part2.setColor(net.md_5.bungee.api.ChatColor.RED);
            part2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "clickable"));
            
            
            part2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("hover text").create()));
            
            TextComponent part3 = new TextComponent("]");
            
            TextComponent all = new TextComponent(part1,part2,part3);

            
            player.spigot().sendMessage(all);
            
            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, all);
            return true;
        }

        return false;
    }

}
