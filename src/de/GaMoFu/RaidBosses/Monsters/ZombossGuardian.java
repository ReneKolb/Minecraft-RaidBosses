package de.GaMoFu.RaidBosses.Monsters;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

import de.GaMoFu.RaidBosses.Skill.ISkill;
import de.GaMoFu.RaidBosses.Skill.SkillHardHit1;

public class ZombossGuardian extends Monster<org.bukkit.entity.PigZombie> {

    public static final String ALIAS = "zombossguard";

    public ZombossGuardian() {
        super(PigZombie.class);
    }

    @Override
    public double getMaxHealth() {
        return 100;
    }

    @Override
    public String getDisplayName() {
        return "Zomboss Guardian";
    }

    @Override
    public double getPullAggroRangeSquared() {
        return 6 * 6;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends LivingEntity>[] getAggroEntityClasses() {
        return new Class[] { Player.class };
    }

    @Override
    protected void afterSpawn() {
        this.entity.setBaby(false);

        this.entity.getEquipment().clear();
        setItemInMainHand(Material.IRON_SWORD, false);

    }

    @Override
    protected int getHealthBarSize() {
        return 55;
    }

    @Override
    public List<ISkill> createSkillList() {
        return Arrays.asList(new SkillHardHit1());
    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_PIGMAN_ANGRY, 1f, 0.8f);
    }

}
