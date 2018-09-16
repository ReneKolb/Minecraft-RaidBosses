package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RestoreHungerPassiveEffect extends ItemEffect {

    public static final RestoreHungerPassiveEffect INSTANCE = new RestoreHungerPassiveEffect();

    private RestoreHungerPassiveEffect() {

    }

    @Override
    public String getTootipText() {
        return ChatColor.YELLOW + "Restore 2 Hunger every second";
    }

    @Override
    public int getTickDelay() {
        return 20;
    }

    @Override
    public void onTick(Player player) {
        player.setFoodLevel(Math.min(20, player.getFoodLevel() + 2));
    }
}
