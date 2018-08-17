package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.Portal;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.SpawnedMonster;
import de.GaMoFu.RaidBosses.Monsters.MonsterType;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.Tooltip.SkillTooltipBuilder;

public class OpenShadowPortal implements ISkill {

    public static final double MAX_SPAWN_PORTAL_RANGE = 5;
    public static final double PORTAL_HEALTH = 200;

    private Random rnd = new Random();

    @Override
    public String getSkillDisplayName() {
        return "Open Shadow Portal";
    }

    @Override
    public String getSkillInternalName() {
        return "OPEN_SHADOW_PORTAL";
    }

    @Override
    public boolean execute(Player executer) {
        return false;
    }

    @Override
    public boolean execute(SpawnedMonster executer) {
        double r = 2 + (MAX_SPAWN_PORTAL_RANGE - 2) * rnd.nextDouble();
        double phi = 2 * Math.PI * rnd.nextDouble();

        Location loc = executer.getMonsterEntity().getEntity().getLocation().add(r * Math.cos(phi), 0,
                r * Math.sin(phi));

        return this.execute(executer, loc);
    }

    @Override
    public boolean execute(SpawnedMonster executer, Location targetLoc) {

        System.out.println("spawn portal");

        Portal portal = new Portal(RaidBosses.getPluginInstance(), executer.getDungeon(), targetLoc, PORTAL_HEALTH,
                20 * 7, 2, 6, Arrays.asList(MonsterType.LESSER_LIVING_SHADOW));

        RaidBosses.getPluginInstance().getPortalHandler()
                .spawnPortal(executer.getMonsterEntity().getEntity().getUniqueId(), portal);
        return true;
    }

    @Override
    public int getBasicHungerCost() {
        return 0;
    }

    @Override
    public int getCooldownTicks() {
        return 20 * 45;
    }

    @Override
    public Material getDisplayMaterial() {
        return null;
    }

    @Override
    public SkillTooltipBuilder getTooltipBuilder() {
        return null;
    }

    @Override
    public boolean isTriggerGlobalCooldown() {
        return true;
    }

}
