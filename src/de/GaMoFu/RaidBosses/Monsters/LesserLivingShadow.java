package de.GaMoFu.RaidBosses.Monsters;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.GaMoFu.RaidBosses.ParticleEffects.SphereEffect;
import de.GaMoFu.RaidBosses.Skill.ISkill;

public class LesserLivingShadow extends Monster<org.bukkit.entity.Zombie> {

    public static final String ALIAS = "LesserLivingShadow";

    private int effectTmr;

    private ArmorStand nameTagEntity;

    public LesserLivingShadow() {
        super(org.bukkit.entity.Zombie.class);
        this.effectTmr = 0;
    }

    @Override
    public List<ISkill> createSkillList() {
        return null;
    }

    @Override
    public double getMaxHealth() {
        return 35;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_PURPLE + "Lesser Living Shadow";
    }

    @Override
    public double getPullAggroRangeSquared() {
        return 100;
    }

    @Override
    protected void setCustomName(String customName) {
        if (this.nameTagEntity == null)
            return;
        this.nameTagEntity.setCustomName(customName);
    }

    @Override
    protected void setCustomNameVisible(boolean visible) {
        if (this.nameTagEntity == null)
            return;
        this.nameTagEntity.setCustomNameVisible(visible);
    }

    @Override
    protected void afterSpawn() {
        this.entity.setBaby(false);

        this.entity.getEquipment().clear();

        this.entity.setSilent(true);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false),
                true);

        this.entity.setCanPickupItems(false);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);
        this.entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);

        this.nameTagEntity = (ArmorStand) this.entity.getWorld().spawnEntity(this.entity.getLocation(),
                EntityType.ARMOR_STAND);
        this.nameTagEntity.setVisible(false);
        this.nameTagEntity.setMarker(true); // very small collision box
        this.nameTagEntity.setCustomName(this.getDisplayName());

        this.entity.addPassenger(nameTagEntity);
    }

    @Override
    public void loop() {
        super.loop();

        this.effectTmr++;
        if (this.effectTmr >= 2) {
            this.effectTmr = 0;

            Location loc = this.getEntity().getLocation();
            SphereEffect.doEffect(loc.add(0, 1.3, 0), Particle.SPELL_WITCH, 1, 0, 0, 0, 15, 0.3);
        }

    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        super.onDamage(event);

        this.entity.getWorld().playSound(this.entity.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 1.3f);
        SphereEffect.doEffect(this.entity.getLocation().add(0, 1.4, 0), Particle.CLOUD, 1, 0, 0, 0, 15, 0.35);
    }

    @Override
    public void onDeath(EntityDeathEvent event) {
        super.onDeath(event);
        this.nameTagEntity.remove();

        this.entity.getWorld().playSound(this.entity.getLocation(), Sound.ENTITY_VEX_DEATH, 1, 1);

    }

    @Override
    protected void playOnFightStartSound(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_AMBIENT, 1f, 0.8f);

    }

    @Override
    public LootTable getLootTable() {
        return null;
    }

}
