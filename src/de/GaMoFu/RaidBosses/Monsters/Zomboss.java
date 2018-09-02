package de.GaMoFu.RaidBosses.Monsters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class Zomboss extends Boss<Zombie> {

    public static final String ALIAS = "zomboss";

//    private List<ItemStack> lootList;
    private List<ISkill> skillList;

    public Zomboss() {
        super(Zombie.class);
//
//        this.lootList = new ArrayList<>();
//        this.lootList.add(new ItemStack(Material.COOKED_BEEF, 4));
//        this.lootList.add(new ItemStack(Material.CARROT, 5)); // Carrot Item?
//        this.lootList.add(new ItemStack(Material.BREAD, 4));
//        this.lootList.add(new ItemStack(Material.COOKED_CHICKEN, 5));
//        this.lootList.add(new ItemStack(Material.ROTTEN_FLESH, 6));

        this.skillList = new ArrayList<>();
        // TODO: add zomboss skills
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    protected void afterSpawn() {
//        System.out.println("Zomboss just spawned");

        this.entity.setBaby(false);

        this.entity.setCustomNameVisible(true);

        // 90% immune to knockback
        this.entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
                .addModifier(new AttributeModifier("Zomboss KnockImmune", 0.9, Operation.ADD_NUMBER));

        this.entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0);

        this.entity.getEquipment().clear();

        setItemInMainHand(Material.GOLDEN_SWORD, true);

        // this.entity.getAttribute(Attribute.
        CraftZombie cz = (CraftZombie) this.entity;
        // increase (virtual) size and therefore attack range
        cz.getHandle().width += 0.5;

    }

    @Override
    protected BossBar createBossBar() {
        return RaidBosses.getPluginInstance().getServer().createBossBar(getDisplayName(), BarColor.BLUE,
                BarStyle.SEGMENTED_10, BarFlag.PLAY_BOSS_MUSIC, BarFlag.DARKEN_SKY);
    }

    @Override
    public double getMaxHealth() {
        return 500;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + "Zomboss";
    }

    @Override
    public void onLoseTarget(Entity oldTarget) {
//        System.out.println("lose target");
    }

    @Override
    public void onChangeTarget(Entity oldTarget, Entity newTarget) {
//        System.out.println("change target");
    }

    // @Override
    // public void onFindTarget(Entity newTarget) {
    // System.out.println("find target");
    // updateHealthBar();
    // for (Player p : this.entity.getWorld().getPlayers()) {
    // bossBar.addPlayer(p);
    // }
    //
    // }

    // @Override
    // public void onDeath(EntityDeathEvent event) {
    // System.out.println("death");
    // bossBar.setProgress(0);
    // }

    // @Override
    // public void onDamage(EntityDamageEvent event) {
    // System.out.println("damage");
    // RaidBosses.getPluginInstance().getServer().getScheduler()
    // .scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {
    //
    // @Override
    // public void run() {
    // updateHealthBar();
    // }
    //
    // },1);
    // }

    @Override
    public double getPullAggroRangeSquared() {
        return 5 * 5;
    }

    // @Override
    // public void onFightStart() {
    // System.out.println("fight start");
    // updateHealthBar();
    // for (Player p : this.entity.getWorld().getPlayers()) {
    // bossBar.addPlayer(p);
    // }
    // }

    // @Override
    // public void onFightEnd(boolean reasonIsBossDeath) {
    // System.out.println("fight end");
    // // Hide the health bar after 1.5sec
    // Bukkit.getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(),
    // new Runnable() {
    //
    // @Override
    // public void run() {
    // bossBar.removeAll();
    // }
    //
    // }, 30);
    //
    // }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        System.out.println("damage by ent");
    }

    @Override
    public List<ISkill> createSkillList() {
        return skillList;
    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 1f, 0.8f);
    }

    @Override
    public LootTable getLootTable() {
        return new CustomLootTable(4, 5).addSaveDrop("TOKEN_LVL_1", 15);
    }

}
