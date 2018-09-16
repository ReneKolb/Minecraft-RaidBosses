package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.entity.Player;

public class RestoreHungerOverhealPassiveEffect extends ItemEffect {

    public static final RestoreHungerOverhealPassiveEffect INSTANCE = new RestoreHungerOverhealPassiveEffect();

    private RestoreHungerOverhealPassiveEffect() {

    }

    @Override
    public String getTootipText() {
        return "Restore 1 Hunger every second. Overheal will heal the lowest group member within 10m.";
    }

    @Override
    public int getTickDelay() {
        return 20;
    }

    @Override
    public void onTick(Player player) {
        if (player.getFoodLevel() < 20) {
            player.setFoodLevel(Math.min(20, player.getFoodLevel() + 1));
            return;
        }

        // Handle overheal: share in group and heal lowest player within 10m
        int lowestFood = 20;
        Player lowestPlayer = null;

        for (Player p : player.getWorld().getPlayers()) {
            if (p.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }

            if (p.getLocation().distanceSquared(player.getLocation()) > 100) {
                continue;
            }

            if (p.getFoodLevel() < lowestFood) {
                lowestFood = p.getFoodLevel();
                lowestPlayer = p;
            }

        }

        if (lowestPlayer != null) {
            lowestPlayer.setFoodLevel(Math.min(20, lowestPlayer.getFoodLevel() + 1));
        }

    }

    @Override
    public String getUniqueEffectID() {
        return "bea4d972-0d2a-49a5-bd64-5781b52a46d3";
    }
}
