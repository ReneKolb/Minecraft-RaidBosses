package de.GaMoFu.RaidBosses.Monsters;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import de.GaMoFu.RaidBosses.Aggro;
import de.GaMoFu.RaidBosses.IdleWalk;
import de.GaMoFu.RaidBosses.RaidBosses;
import de.GaMoFu.RaidBosses.Config.IdleWalkSettings;
import de.GaMoFu.RaidBosses.Events.BossDeathEvent;
import de.GaMoFu.RaidBosses.Events.FightEndEvent;
import de.GaMoFu.RaidBosses.Events.FightStartEvent;
import de.GaMoFu.RaidBosses.Events.MonsterDeathEvent;
import de.GaMoFu.RaidBosses.Skill.ISkill;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_13_R2.PathfinderGoalSelector;

public abstract class Monster<T extends Creature> implements Listener {

    // public static final int HEALTH_BAR_SIZE = 30; // normal 30, champion 55,
    // elite 80

    // Important: T and EntityType must match!!
    protected EntityType entityType;

    protected Aggro aggro;

    protected Class<T> type;

    protected T entity;

    public abstract LootTable getLootTable();

    public abstract List<ISkill> createSkillList();

    private List<ISkill> internalSkillList;

    public List<ISkill> getSkills() {
        if (internalSkillList == null) {
            List<ISkill> createdList = createSkillList();
            if (createdList == null)
                internalSkillList = new LinkedList<>();
            else
                internalSkillList = new LinkedList<>(createdList);
        }
        return internalSkillList;
    }

    protected void updateSkills(List<ISkill> newSkills) {
        this.internalSkillList.clear();
        this.internalSkillList.addAll(newSkills);
    }

    public Monster(Class<T> type) {
        this.type = type;
        this.aggro = new Aggro();
    }

    protected IdleWalk<? extends IdleWalkSettings> idleWalk;

    private int aggroRangeCheck;

    protected boolean inFight;

    protected Location spawnedLocation;

    public abstract double getMaxHealth();

    public abstract String getDisplayName();

    public abstract double getPullAggroRangeSquared();

    public double getLoseAggroRangeSquared() {
        double range = this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getValue();
        return range * range;
    }

    protected LivingEntity lastTarget;

    private int loopTaskID = -1;

    protected void removePathfinderGoal(Class<? extends PathfinderGoal> goalClass) {
        if (this.entity == null)
            return;

        CraftCreature entity = (CraftCreature) this.entity;

        try {
            Field b = PathfinderGoalSelector.class.getDeclaredField("b");
            b.setAccessible(true); // since "b" is private final

            Set<?> fieldB = (Set<?>) b.get(entity.getHandle().goalSelector);

            Class<?> clazz = Class
                    .forName("net.minecraft.server.v1_13_R2.PathfinderGoalSelector$PathfinderGoalSelectorItem");
            Field a = clazz.getField("a");
            a.setAccessible(true); // Since "a" is public final

            for (Object o : new HashSet<>(fieldB)) {
                PathfinderGoal goal = (PathfinderGoal) a.get(o);
                if (goalClass.isInstance(goal)) {
                    fieldB.remove(o);
//                    System.out.println("Removed: " + goal);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isInFight() {
        return this.inFight;
    }

    protected void removePathfinderGoal(String qualifiedClassName) {
        try {
            Class<? extends PathfinderGoal> clazz = (Class<? extends PathfinderGoal>) Class.forName(qualifiedClassName);
            removePathfinderGoal(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    
    public static void clearGoalSelectors(CraftCreature entity) {
        try {
            Field b = PathfinderGoalSelector.class.getDeclaredField("b");
            b.setAccessible(true); // since "b" is private final

            Set<?> fieldB = (Set<?>) b.get(entity.getHandle().goalSelector);

            Class<?> clazz = Class
                    .forName("net.minecraft.server.v1_13_R2.PathfinderGoalSelector$PathfinderGoalSelectorItem");
            Field a = clazz.getField("a");
            a.setAccessible(true); // Since "a" is public final

            fieldB.clear();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void clearTargetSelectors(CraftCreature entity) {
        try {
            Field b = PathfinderGoalSelector.class.getDeclaredField("b");

            b.setAccessible(true); // since "b" is private final

            Set<?> fieldB = (Set<?>) b.get(entity.getHandle().targetSelector);

            fieldB.clear();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Clears the automatic target selection of vanilla minecraft entity.
     * <p>
     * We do the target selection via {@link Aggro}
     */
    protected void clearTargetSelectors() {
        if (this.entity == null)
            return;

        CraftCreature entity = (CraftCreature) this.entity;

        clearTargetSelectors(entity);
    }

    protected void removeRandomMovement() {
        removePathfinderGoal(PathfinderGoalRandomStroll.class);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (this.entity == null)
            return;

        // prevent the entity from burning in sunlight and also from lava damage?
        if (event.getEntity().getUniqueId().equals(this.entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    public void spawn(Location location) {
//        System.out.println("Spawning monster");

        Bukkit.getServer().getPluginManager().registerEvents(this, RaidBosses.getPluginInstance());
        aggroRangeCheck = 0;
        World world = location.getWorld();
        this.entity = (T) world.spawn(location, type);

        this.spawnedLocation = location.clone();

        removeRandomMovement();
        clearTargetSelectors();

        // dont remove the entity when its far away. always loaded.
        this.entity.setRemoveWhenFarAway(false);

        this.entity.setCanPickupItems(false);

        this.entity.getEquipment().clear();

        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        this.entity.setHealth(getMaxHealth());

        this.entity.setCustomName(this.getDisplayName());

        this.inFight = false;

        afterSpawn();
    }

    protected abstract void playOnFightStartSound(Location loc);

    public void onFightStart() {
        Bukkit.getPluginManager().callEvent(new FightStartEvent(this));
        
        Location eyeLoc = this.entity.getEyeLocation();
        World w = eyeLoc.getWorld();
        w.spawnParticle(Particle.VILLAGER_ANGRY, eyeLoc, 5);
        playOnFightStartSound(eyeLoc);
    };

    public void onFightEnd(boolean reasonIsBossDeath) {
        Bukkit.getPluginManager().callEvent(new FightEndEvent(this, reasonIsBossDeath));
    };

    public void onLoseTarget(Entity oldTarget) {
    };

    public void onChangeTarget(Entity oldTarget, Entity newTarget/* TODO: reason? */) {
    };

    public void onFindTarget(Entity newTarget) {
    };

    public void onDeath(EntityDeathEvent event) {
    };

    public void onDamage(EntityDamageEvent event) {
    };

    public void onDamageByEntity(EntityDamageByEntityEvent event) {
    };

    protected Optional<LivingEntity> getEntityByUUID(UUID id) {
        if (this.entity == null)
            return Optional.empty();

        // PERF: is there maybe already a lookup map inside World?
        for (LivingEntity e : entity.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (e.getUniqueId().equals(id)) {
                return Optional.of(e);
            }
        }

        // May occur when the id is not valid, or the entity is not (any more) inside
        // the world
        return Optional.empty();
    }

    protected boolean canSee(Location loc) {
        if (!this.getEntity().getWorld().getName().equals(loc.getWorld().getName()))
            return false;

        Vector dir = loc.toVector().subtract(this.getEntity().getLocation().toVector());
        int dist = (int) dir.length();

        BlockIterator blockIterator = new BlockIterator(this.getEntity().getWorld(),
                this.getEntity().getLocation().toVector(), dir, this.getEntity().getEyeHeight(), dist);

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.getType().isOccluding())
                return false;
        }

        return true;
    }

    protected void onNewTarget(UUID newTargetID) {
        if (newTargetID == null) {

            onLoseTarget(lastTarget);

            entity.setTarget(null);
            lastTarget = null;
            aggro.reset();
            // new Target is null. Clear aggro. No more players with aggro (all dead?). The
            // fight ends.
            onFightEnd(false);
            // on FightEnd Heal fully
            double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            entity.setHealth(maxHealth);
            Bukkit.getServer().getPluginManager()
                    .callEvent(new EntityRegainHealthEvent(entity, maxHealth, RegainReason.REGEN));

            // Walk back to spawned Location
            ((CraftCreature) entity).getHandle().getNavigation().a(getSpawnedLocation().getX(),
                    getSpawnedLocation().getY(), getSpawnedLocation().getZ(), 1);

            inFight = false;
            return;
        }

        if (lastTarget != null && newTargetID.equals(lastTarget.getUniqueId())) {
            // No new Target. Is still the same
            return;
        }

        LivingEntity newTarget = getEntityByUUID(newTargetID).orElse(null);

        entity.setTarget(newTarget);

        if (lastTarget == null) {
            inFight = true;
            onFindTarget(newTarget);
        } else {
            onChangeTarget(lastTarget, newTarget);
        }

        lastTarget = newTarget;
    }

    protected boolean showHealthAsName() {
        return true; // overridden by Bosses
    }

    @SuppressWarnings("unchecked")
    public Class<? extends LivingEntity>[] getAggroEntityClasses() {
        return new Class[] { Player.class };
    }

    protected boolean looseAggro(LivingEntity entity) {

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getGameMode() == GameMode.CREATIVE) {
                return true;
            }
        }

        if (entity.isDead()) {
            return true;
        }

        if (!entity.getLocation().getWorld().getName().equals(this.entity.getWorld().getName())) {
            return true;
        }

        if (entity.getLocation().distanceSquared(this.entity.getLocation()) > this.getLoseAggroRangeSquared()) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @return The amount of "|" characters in the Health display. A Monster may
     *         override this method to display another amount.
     */
    protected int getHealthBarSize() {
        return 30;// normal 30, champion 55, elite 80
    }

    /**
     * 
     * @param showHealth
     *            true if show the Health bar. false if the original Name should be
     *            displayed
     */
    protected void updateHealthDisplay(boolean showHealth) {
        if (!showHealthAsName())
            return;

        if (showHealth) {
            this.entity.setCustomNameVisible(true);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RaidBosses.getPluginInstance(), new Runnable() {

                @Override
                public void run() {
                    entity.setCustomName(buildHealthDisplayName(getHealthBarSize()));
                }

            });

        } else {
            this.entity.setCustomName(this.getDisplayName());
            this.entity.setCustomNameVisible(false);
        }
    }

    public void loop() {
        if (this.entity == null)
            return;

        handleAggroLoop();

        if (!inFight && this.idleWalk != null) {
            this.idleWalk.loop();
        }
    }

    private void handleAggroLoop() {
        aggroRangeCheck++;
        if (aggroRangeCheck > 10) {
            aggroRangeCheck = 0;

            if (lastTarget == null) {
                // No current target, check if a player is in aggro range
                Entity nearestEntity = null;
                double nearestDistance = Double.MAX_VALUE;

                for (Entity e : entity.getWorld().getEntitiesByClasses(getAggroEntityClasses())) {
                    if (e instanceof Player) {
                        if (((Player) e).getGameMode() == GameMode.CREATIVE) {
                            // a creative player cannot get aggro
                            continue;
                        }
                    }
                    Location targetLoc;
                    if (e instanceof LivingEntity) {
                        targetLoc = ((LivingEntity) e).getEyeLocation();
                    } else {
                        targetLoc = e.getLocation();
                    }

                    double dist = targetLoc.distanceSquared(this.entity.getLocation());
                    if (dist <= getPullAggroRangeSquared() && dist < nearestDistance && canSee(targetLoc)) {
                        nearestDistance = dist;
                        nearestEntity = e;
                    }
                }

                if (nearestEntity != null) {
                    UUID newTarget = this.aggro.addAggro(nearestEntity.getUniqueId(), 1);
                    if (inFight == false) {
                        onFightStart();
                    }
                    inFight = true;
                    onNewTarget(newTarget);
                }
            } else {
                // has a current target, check if it is still in aggro range
                if (looseAggro(lastTarget)) {
                    UUID newTarget = this.aggro.removeDamager(lastTarget.getUniqueId());
                    // may also be null if no other player with aggro exists;
                    // onLoseTarget(lastTarget);
                    onNewTarget(newTarget);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (this.entity == null)
            return;

        if (this.entity.getUniqueId().equals(event.getEntity().getUniqueId())) {
            if (loopTaskID != -1) {
                Bukkit.getServer().getScheduler().cancelTask(this.loopTaskID);
                loopTaskID = -1;
            }
            onDeath(event);
            onFightEnd(true);

            Bukkit.getServer().getPluginManager().callEvent(new MonsterDeathEvent(this));
            
            if (this instanceof Boss) {
                Boss<? extends Creature> boss = (Boss<? extends Creature>) this;
                Bukkit.getServer().getPluginManager().callEvent(new BossDeathEvent(boss));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.entity == null)
            return;

        if (this.entity.getUniqueId().equals(event.getEntity().getUniqueId())) {
            onDamage(event);
            updateHealthDisplay(true);

            onReceiveDamage();
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (this.entity == null)
            return;

        if (this.entity.getUniqueId().equals(event.getEntity().getUniqueId())) {
            double health = this.entity.getHealth();
            double maxHealth = this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

            updateHealthDisplay(health < maxHealth);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (this.entity == null)
            return;

        if (this.entity.getUniqueId().equals(event.getEntity().getUniqueId())) {
            onDamageByEntity(event);
            if (!event.isCancelled()) {
                Entity damager = event.getDamager();
                if (damager instanceof Projectile) {
                    Projectile projectile = (Projectile) damager;
                    ProjectileSource shooter = projectile.getShooter();
                    if (shooter instanceof LivingEntity) {
                        LivingEntity shooterEntity = (LivingEntity) shooter;
                        UUID newTarget = this.aggro.addAggro(shooterEntity.getUniqueId(), event.getFinalDamage());
                        onNewTarget(newTarget);
                    } else {
                        RaidBosses.getPluginInstance().getServer().getLogger()
                                .info("The Shooter is not an LivingEntity");
                    }
                } else if (damager instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) damager;
                    UUID newTarget = this.aggro.addAggro(livingEntity.getUniqueId(), event.getFinalDamage());
                    onNewTarget(newTarget);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.entity == null)
            return;

        UUID newTarget = this.aggro.removeDamager(event.getEntity().getUniqueId());
        onNewTarget(newTarget);

        Player player = event.getEntity();
        event.setDeathMessage(player.getName() + " was slain by " + this.getDisplayName());

    }

    /**
     * This method is called after the monster has been spawned.
     * <p>
     * Implement equipment, stats etc here.
     */
    protected abstract void afterSpawn();

    protected String buildHealthDisplayName(int size) {
        double health = this.entity.getHealth();
        double maxHealth = this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if (health < 0) {
            health = 0;
        }
        // final int MaxBarsCount = 30;
        double healthPercent = health / maxHealth;
        int count = (int) Math.round(healthPercent * size);

        if (healthPercent > 0 && count == 0) {
            count = 1; // nur keine Balken wenn wirklich tot
        }
        StringBuffer buffer = new StringBuffer();

        if (healthPercent <= 0.2) {
            buffer.append(ChatColor.RED.toString());
        } else if (healthPercent <= 0.6) {
            buffer.append(ChatColor.YELLOW.toString());
        } else {
            buffer.append(ChatColor.GREEN.toString());
        }

        for (int i = 0; i < count; i++) {
            buffer.append("|");
        }

        if (count < size) {
            buffer.append(ChatColor.BLACK.toString());
            for (int i = count; i < size; i++) {
                buffer.append("|");
            }
        }

        return buffer.toString();
    }

    public T getEntity() {
        return this.entity;
    }

    /**
     * Get a copy of the spawned Location
     * 
     * @return
     */
    public Location getSpawnedLocation() {
        return this.spawnedLocation.clone();
    }

    /**
     * @param idleWalk
     *            the idleWalk to set
     */
    public void setIdleWalk(IdleWalk<? extends IdleWalkSettings> idleWalk) {
        this.idleWalk = idleWalk;
    }

    public UUID getRandomAggroTarget() {
        return aggro.getRandomTarget();
    }

    public long getGlobalCooldownTicks() {
        // May be overridden to use other global cooldowns
        return 40;
    }

    public void onReceiveDamage() {
    }

    public void setItemInMainHand(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setItemInMainHand(s);
    }

    public void setItemInMainHand(ItemStack stack) {
        this.entity.getEquipment().setItemInMainHand(stack);
        this.entity.getEquipment().setItemInMainHandDropChance(0f);
    }

    public void setItemInOffHand(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setItemInOffHand(s);
    }

    public void setItemInOffHand(ItemStack stack) {
        this.entity.getEquipment().setItemInOffHand(stack);
        this.entity.getEquipment().setItemInOffHandDropChance(0f);
    }

    public void setHelmet(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setHelmet(s);
    }

    public void setHelmet(Color color, boolean glow) {
        ItemStack s = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) s.getItemMeta();
        meta.setColor(color);
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setHelmet(s);
    }

    public void setHelmet(ItemStack stack) {
        this.entity.getEquipment().setHelmet(stack);
        this.entity.getEquipment().setHelmetDropChance(0f);
    }

    public void setChestplate(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setChestplate(s);
    }

    public void setChestplate(Color color, boolean glow) {
        ItemStack s = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) s.getItemMeta();
        meta.setColor(color);
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setChestplate(s);
    }

    public void setChestplate(ItemStack stack) {
        this.entity.getEquipment().setChestplate(stack);
        this.entity.getEquipment().setChestplateDropChance(0f);
    }

    public void setLeggings(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setLeggings(s);
    }

    public void setLeggings(Color color, boolean glow) {
        ItemStack s = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) s.getItemMeta();
        meta.setColor(color);
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setLeggings(s);
    }

    public void setLeggings(ItemStack stack) {
        this.entity.getEquipment().setLeggings(stack);
        this.entity.getEquipment().setLeggingsDropChance(0f);
    }

    public void setBoots(Material mat, boolean glow) {
        ItemStack s = new ItemStack(mat);
        ItemMeta meta = s.getItemMeta();
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setBoots(s);
    }

    public void setBoots(Color color, boolean glow) {
        ItemStack s = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) s.getItemMeta();
        meta.setColor(color);
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        s.setItemMeta(meta);
        this.setBoots(s);
    }

    public void setBoots(ItemStack stack) {
        this.entity.getEquipment().setBoots(stack);
        this.entity.getEquipment().setBootsDropChance(0f);
    }

}
