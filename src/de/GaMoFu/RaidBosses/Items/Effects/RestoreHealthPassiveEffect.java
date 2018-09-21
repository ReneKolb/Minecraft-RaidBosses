package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class RestoreHealthPassiveEffect extends ItemEffect {

    public static final RestoreHealthPassiveEffect INSTANCE = new RestoreHealthPassiveEffect();

    private RestoreHealthPassiveEffect() {

    }

    @Override
    public String getTootipText() {
        return "Restore " + SkillTooltipBuilder.formatHealth(1) + ChatColor.YELLOW + " every 2 second";
    }

    @Override
    public int getTickDelay() {
        return 40;
    }

    @Override
    public void onTick(Player player) {
        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (player.getHealth() < playerMaxHealth) {
            player.setHealth(Math.min(playerMaxHealth, player.getHealth() + 1));

            Bukkit.getServer().getPluginManager().callEvent(new EntityRegainHealthEvent(player, 1, RegainReason.MAGIC));
        }
    }

    @Override
    public String getUniqueEffectID() {
        return "e2e3ce9b-7f4a-4c05-b9f8-ed787b53af55";
    }
}