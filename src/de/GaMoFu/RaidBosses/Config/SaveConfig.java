package de.GaMoFu.RaidBosses.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SaveConfig {

    private Collection<MonsterConfig> monsterConfig;

    private Collection<MonsterConfig> bossesConfig;

    private String resourcePackPath;

    private Map<String, Map<String, SaveLocation>> namedLocations;

    public SaveConfig(Collection<MonsterConfig> monsterConfig, Collection<MonsterConfig> bossesConfig,
            String resourcePackPath, Map<String, Map<String, SaveLocation>> namedLocations) {
        this.monsterConfig = monsterConfig;
        this.bossesConfig = bossesConfig;
        this.resourcePackPath = resourcePackPath;
        this.namedLocations = namedLocations;
    }

    /**
     * @return the monsterConfig
     */
    public Collection<MonsterConfig> getMonsterConfig() {
        if (monsterConfig == null) {
            monsterConfig = new ArrayList<>();
        }
        return monsterConfig;
    }

    /**
     * @return the bossesConfig
     */
    public Collection<MonsterConfig> getBossesConfig() {
        if (bossesConfig == null) {
            bossesConfig = new ArrayList<>();
        }
        return bossesConfig;
    }

    /**
     * @return the ressourcePackPath
     */
    public String getResourcePackPath() {
        return resourcePackPath;
    }

    /**
     * @param ressourcePackPath
     *            the ressourcePackPath to set
     */
    public void setRessourcePackPath(String resourcePackPath) {
        this.resourcePackPath = resourcePackPath;
    }

    /**
     * @return the namedLocations
     */
    public Map<String, Map<String, SaveLocation>> getNamedLocations() {
        if (namedLocations == null) {
            namedLocations = new HashMap<>();
        }
        return namedLocations;
    }

}
