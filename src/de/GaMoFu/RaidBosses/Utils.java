package de.GaMoFu.RaidBosses;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import net.minecraft.server.v1_13_R1.DamageSource;

public class Utils {

	public static String LocationToString(Location loc) {
		String worldName = loc.getWorld().getName();

		int blockX = loc.getBlockX();
		double x = loc.getX();

		int blockY = loc.getBlockY();
		double y = loc.getY();

		int blockZ = loc.getBlockZ();
		double z = loc.getZ();

		// float yaw = loc.getYaw();
		// float pitch = loc.getPitch();

		StringBuilder builder = new StringBuilder();

		builder.append("(").append(ChatColor.AQUA).append(worldName).append(ChatColor.RESET).append(") ");
		builder.append("[").append(blockX).append("]");
		builder.append(String.format("%.2f", x));
		builder.append(" [").append(blockY).append("]");
		builder.append(String.format("%.2f", y));
		builder.append(" [").append(blockZ).append("]");
		builder.append(String.format("%.2f", z));

		return builder.toString();
	}

	public static void sendLocation(CommandSender sender, Location loc) {
		String worldName = loc.getWorld().getName();

		int blockX = loc.getBlockX();
		double x = loc.getX();

		int blockY = loc.getBlockY();
		double y = loc.getY();

		int blockZ = loc.getBlockZ();
		double z = loc.getZ();

		float yaw = loc.getYaw();
		float pitch = loc.getPitch();

		sender.sendMessage("Location in '" + ChatColor.BLUE + worldName + ChatColor.RESET + "'");
		sender.sendMessage("  X: [" + blockX + "] " + x);
		sender.sendMessage("  Y: [" + blockY + "] " + y);
		sender.sendMessage("  Z: [" + blockZ + "] " + z);
		sender.sendMessage("  yaw:   " + yaw);
		sender.sendMessage("  pitch: " + pitch);
	}

	//@formatter:off
	public static List<Material> interactableBlocks = Arrays.asList(
            Material.ANVIL,
            Material.BEACON,
            
            Material.BLACK_BED,Material.BLUE_BED,Material.BROWN_BED,Material.CYAN_BED,Material.GREEN_BED,Material.GRAY_BED,Material.LIME_BED,Material.MAGENTA_BED,Material.ORANGE_BED,Material.PINK_BED,Material.PURPLE_BED,Material.RED_BED,Material.WHITE_BED,Material.YELLOW_BED,Material.LIGHT_BLUE_BED,Material.LIGHT_GRAY_BED,
            
            Material.ACACIA_BOAT,Material.BIRCH_BOAT,Material.JUNGLE_BOAT,Material.OAK_BOAT,Material.SPRUCE_BOAT,Material.DARK_OAK_BOAT,

            Material.BREWING_STAND,
            
            Material.COMMAND_BLOCK,Material.COMMAND_BLOCK_MINECART,
            
            Material.CHEST,
            
            Material.ACACIA_DOOR, Material.BIRCH_DOOR,Material.IRON_DOOR,Material.JUNGLE_DOOR,Material.OAK_DOOR,Material.SPRUCE_DOOR,Material.DARK_OAK_DOOR,
            
            Material.DAYLIGHT_DETECTOR,
            Material.DISPENSER,
            Material.DROPPER,
            Material.ENCHANTING_TABLE,
            Material.ENDER_CHEST,
            
            Material.ACACIA_FENCE_GATE,Material.BIRCH_FENCE_GATE,Material.JUNGLE_FENCE_GATE,Material.OAK_FENCE_GATE,Material.SPRUCE_FENCE_GATE,Material.DARK_OAK_FENCE_GATE,
            
            Material.FURNACE,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.ITEM_FRAME,
            Material.LEVER,
            Material.MINECART,
            Material.FURNACE_MINECART,
            Material.CHEST_MINECART,
            Material.NOTE_BLOCK,
            Material.COMPARATOR,
            Material.SIGN,
            Material.WALL_SIGN,
            
            Material.ACACIA_TRAPDOOR,Material.BIRCH_TRAPDOOR,Material.IRON_TRAPDOOR,Material.JUNGLE_TRAPDOOR,Material.OAK_TRAPDOOR,Material.SPRUCE_TRAPDOOR,Material.DARK_OAK_TRAPDOOR,
            
            Material.TRAPPED_CHEST,
            
            Material.BIRCH_BUTTON,Material.BIRCH_BUTTON,Material.JUNGLE_BUTTON,Material.OAK_BUTTON,Material.SPRUCE_BUTTON,Material.STONE_BUTTON,Material.DARK_OAK_BUTTON,
            
            Material.CRAFTING_TABLE);
	//@formatter:on

	public static void setIgnoreArmor(DamageSource ds) {
		try {
			Method setIgnoreArmor = DamageSource.class.getDeclaredMethod("setIgnoreArmor");
			setIgnoreArmor.invoke(ds);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
