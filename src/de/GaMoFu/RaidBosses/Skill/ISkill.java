package de.GaMoFu.RaidBosses.Skill;

import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public interface ISkill {

    public static final int UNKOWN_LEVEL = Integer.MAX_VALUE;

    String getSkillDisplayNameWithoutLevel();

    default public String getSkillDisplayName() {
        if (getLevel() > 0) {
            return ChatColor.AQUA.toString() + ChatColor.ITALIC + getSkillDisplayNameWithoutLevel() + "   "
                    + ChatColor.RESET + ChatColor.WHITE + "Lv. " + getLevel();
        } else if (getLevel() == UNKOWN_LEVEL) {
            return ChatColor.AQUA.toString() + ChatColor.ITALIC + getSkillDisplayNameWithoutLevel() + "   "
                    + ChatColor.RESET + ChatColor.WHITE + "Lv. " + ChatColor.MAGIC + getLevel();
        } else {
            return ChatColor.AQUA.toString() + ChatColor.ITALIC + getSkillDisplayNameWithoutLevel();
        }
    }

    public String getSkillInternalName();

    public boolean execute(Player executer);

    public boolean execute(SpawnedMonster executer);

    public boolean execute(SpawnedMonster executer, Location targetLoc);

    public int getBasicHungerCost();

    public int getCooldownTicks();

    public Material getDisplayMaterial();

    public int getLevel();

    default public short getDisplayDurability() {
        return 0;
    }

    public SkillTooltipBuilder getTooltipBuilder();

    /**
     * 
     * @param source
     * @param targetMonster
     *            If true monsters are selected. If false Players are selected
     * @return sorted ascending by distance (e.g: 1, 4, 9, 13)
     */
    default SortedSet<Distance<LivingEntity>> getEntitiesInFront(LivingEntity source, boolean targetMonster,
            float range) {
        SortedSet<Distance<LivingEntity>> result = new TreeSet<>(
                (v1, v2) -> Double.compare(v1.getObject2(), v2.getObject2()));

        World world = source.getLocation().getWorld();
        for (LivingEntity le : world.getLivingEntities()) {
            // exclude the source itself
            if (le.getUniqueId().equals(source.getUniqueId()))
                continue;

            if (RaidBosses.getPluginInstance().getHologramHandler().isHologramEntity(le))
                continue;

            if (targetMonster) {
                if (!(le instanceof Player)) {
                    double dist = le.getLocation().distance(source.getLocation());
                    if (dist <= range) {
                        Vector angleVector = le.getLocation().toVector().subtract(source.getLocation().toVector());
                        float angle = angleVector.angle(source.getLocation().getDirection());
                        if (Math.abs(angle) <= 0.52) {
                            result.add(new Distance<LivingEntity>(le, dist));
                        }
                    }
                }
            } else {
                if (le instanceof Player) {
                    double dist = le.getLocation().distance(source.getLocation());
                    if (dist <= range) {
                        Vector angleVector = le.getLocation().toVector().subtract(source.getLocation().toVector());
                        float angle = angleVector.angle(source.getLocation().getDirection());
                        if (Math.abs(angle) <= 0.52) {
                            result.add(new Distance<LivingEntity>(le, dist));
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean isTriggerGlobalCooldown();

}
