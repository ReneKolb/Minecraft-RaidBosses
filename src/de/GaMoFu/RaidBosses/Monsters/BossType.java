package de.GaMoFu.RaidBosses.Monsters;

import java.util.Optional;

import org.bukkit.entity.Creature;

import de.GaMoFu.RaidBosses.Monsters.Bosses.Theal.Theal;;

public enum BossType {

    //@formatter:off
	ZOMBOSS(Zomboss.class, Zomboss.ALIAS),
	THEAL(Theal.class, Theal.ALIAS);
	//@formatter:on

    private Class<? extends Boss<? extends Creature>> entityClass;

    private String aliasName;

    private BossType(Class<? extends Boss<? extends Creature>> monsterClass, String aliasName) {
        this.entityClass = monsterClass;
        this.aliasName = aliasName;
    }

    /**
     * @return the entityClass
     */
    public Class<? extends Boss<? extends Creature>> getEntityClass() {
        return entityClass;
    }

    /**
     * @return the aliasName
     */
    public String getAliasName() {
        return aliasName;
    }

    public static Optional<BossType> fromAliasName(String aliasName) {
        for (BossType type : BossType.values()) {
            if (type.aliasName.equals(aliasName)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}
