package de.GaMoFu.RaidBosses.Dungeons;

public class DungeonConfig {

    private String aliasName;

    private String worldName;

    /**
     * @return the aliasName
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * @param aliasName
     *            the aliasName to set
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * @return the worldName
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * @param worldName
     *            the worldName to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String toSaveString() {
        return aliasName + ";" + worldName;
    }

    public static DungeonConfig fromSaveString(String saveString) {
        String[] split = saveString.split(";");

        if (split.length != 2) {
            throw new RuntimeException("Cannot create InstanceConfig from save String '" + saveString + "'");
        }

        DungeonConfig result = new DungeonConfig();
        result.aliasName = split[0];
        result.worldName = split[1];
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InstanceConfig [aliasName=" + aliasName + ", worldName=" + worldName + "]";
    }

}
