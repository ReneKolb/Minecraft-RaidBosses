package de.GaMoFu.RaidBosses.Items.Effects;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class RestoreHealthPassiveEffect extends ItemEffect {

    public static final RestoreHealthPassiveEffect INSTANCE = new RestoreHealthPassiveEffect();

    private RestoreHealthPassiveEffect() {

    }

    @Override
    public String getTootipText() {
        return ChatColor.YELLOW + "Restore " + SkillTooltipBuilder.formatHealth(1) + ChatColor.YELLOW + " every 2 second";
    }

    @Override
    public int getTickDelay() {
        return 40;
    }

    @Override
    public void onTick(Player player) {
        player.setHealth(
                Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + 1));
    }

    @Override
    public String getUniqueEffectID() {
        return "e2e3ce9b-7f4a-4c05-b9f8-ed787b53af55";
    }
}