package de.GaMoFu.RaidBosses.Monsters.Bosses.Theal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
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
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftHusk;
import org.bukkit.entity.Husk;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Monsters.Boss;
import de.GaMoFu.RaidBosses.Monsters.CustomLootTable;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class Theal extends Boss<Husk> {

    public static final String ALIAS = "THEAL";

    private List<ItemStack> lootList;

    private int phase;

    public Theal() {
        super(Husk.class);

        this.lootList = new ArrayList<>();
        this.phase = 1;

    }

    @Override
    public void onFightStart() {
        super.onFightStart();
        this.phase = 1;
    }

    @Override
    public void loop() {
        super.loop();

        if (this.phase == 1 && this.entity.getHealth() <= 0.9 * getMaxHealth()) {
            // Go to next phase: ICE PHASE
            this.phase = 2;
            this.updateSkills(Arrays.asList(new FrostBolt(), new FrostAuraDamage(false),
                    new FrostPool(new FrostAuraDamage(true))));

            // keep helmet
            setItemInMainHand(Material.SNOWBALL, true);
            setChestplate(Color.fromRGB(0, 255, 255), false);
            setLeggings(Color.fromRGB(0, 255, 255), false);
            setBoots(Color.fromRGB(0, 255, 255), false);
        }

        if (this.phase == 2 && this.entity.getHealth() <= 0.6 * getMaxHealth()) {
            // Go to next phase: FIRE PHASE
            this.phase = 3;

            this.updateSkills(Arrays.asList(new FireBolt(), new FlamePool(new FlameAuraDamage())));

            // keep helmet
            setItemInMainHand(Material.BLAZE_POWDER, true);
            setItemInOffHand(Material.BLAZE_POWDER, true);
            setChestplate(Color.fromRGB(255, 0, 0), false);
            setLeggings(Color.fromRGB(255, 0, 0), false);
            setBoots(Color.fromRGB(255, 0, 0), false);
        }

        if (this.phase == 3 && this.entity.getHealth() <= 0.3 * getMaxHealth()) {
            // Go to next phase: DARK PHASE
            this.phase = 4;

            this.updateSkills(Arrays.asList(new OpenShadowPortal(), new WitherAura(), new DarkShot()));

            // keep helmet
            setItemInMainHand(Material.DIAMOND_HOE, true);
            setItemInOffHand(Material.DIAMOND_HOE, true);
            setChestplate(Color.fromRGB(128, 0, 128), false);
            setLeggings(Color.fromRGB(128, 0, 128), false);
            setBoots(Color.fromRGB(128, 0, 128), false);
        }

    }

    @Override
    protected BossBar createBossBar() {
        return RaidBosses.getPluginInstance().getServer().createBossBar(getDisplayName(), BarColor.BLUE,
                BarStyle.SEGMENTED_10, BarFlag.PLAY_BOSS_MUSIC, BarFlag.DARKEN_SKY);
    }

    @Override
    public List<ISkill> createSkillList() {
        return Arrays.asList();
    }

    @Override
    public double getMaxHealth() {
        return 1200;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_PURPLE + "Theal the Unstable";
    }

    @Override
    public double getPullAggroRangeSquared() {
        return 5 * 5;
    }

    @Override
    protected void afterSpawn() {
        this.entity.setBaby(false);

        this.entity.setCustomNameVisible(true);

        setItemInMainHand(Material.BLAZE_ROD, true);

        // ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 5);
        // this.entity.getEquipment().setHelmet(head);
        // this.entity.getEquipment().setHelmetDropChance(0f);

        setChestplate(Color.fromRGB(182, 184, 179), false);
        setLeggings(Color.fromRGB(182, 184, 179), false);
        setBoots(Color.fromRGB(182, 184, 179), false);

        // immune to knockback
        this.entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
                .addModifier(new AttributeModifier("Theal KnockImmune", 1, Operation.ADD_NUMBER));

        this.entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0);

        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                .addModifier(new AttributeModifier("Theal speed", 0.3, Operation.MULTIPLY_SCALAR_1));
        // Let him stand still for testing his auras etc
        // this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        // .addModifier(new AttributeModifier("Theal speed", -1,
        // Operation.MULTIPLY_SCALAR_1));

        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(6);

        // this.entity.getAttribute(Attribute.
        CraftHusk ch = (CraftHusk) this.entity;
        // increase (virtual) size and therefore attack range
        ch.getHandle().width += 0.5;
    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_HUSK_DEATH, 1f, 0.8f);
    }

    @Override
    public LootTable getLootTable() {
        return new CustomLootTable(4, 5).addSaveDrop("TOKEN_LVL_3", 20);
    }

}
