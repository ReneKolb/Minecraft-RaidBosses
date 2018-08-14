package de.GaMoFu.RaidBosses.Monsters;

import java.util.Optional;

import org.bukkit.entity.Creature;

public enum MonsterType {

    //@formatter:off
	ANNOYING_BUG(AnnoyingBug.class, AnnoyingBug.ALIAS),
	SPIDER(Spider.class, Spider.ALIAS),
	WITCH(Witch.class, Witch.ALIAS),
	ZOMBIE(Zombie.class, Zombie.ALIAS),
	ZOMBOSS_GUARDIAN(ZombossGuardian.class, ZombossGuardian.ALIAS),
	
	LESSER_LIVING_SHADOW(LesserLivingShadow.class, LesserLivingShadow.ALIAS);
	//@formatter:on

    private Class<? extends Monster<? extends Creature>> entityClass;

    private String aliasName;

    private MonsterType(Class<? extends Monster<? extends Creature>> monsterClass, String aliasName) {
        this.entityClass = monsterClass;
        this.aliasName = aliasName;
    }

    /**
     * @return the entityClass
     */
    public Class<? extends Monster<? extends Creature>> getEntityClass() {
        return entityClass;
    }

    /**
     * @return the aliasName
     */
    public String getAliasName() {
        return aliasName;
    }

    public static Optional<MonsterType> fromAliasName(String aliasName) {
        for (MonsterType type : MonsterType.values()) {
            if (type.aliasName.equals(aliasName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}
