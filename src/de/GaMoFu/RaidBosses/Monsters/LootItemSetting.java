package de.GaMoFu.RaidBosses.Monsters;

public class LootItemSetting {

    private String templateName;

    private int stackSize;

    private int weight;

    public LootItemSetting(String templateName, int stackSize, int weight) {
        this.templateName = templateName;
        this.stackSize = stackSize;
        this.weight = weight;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @return the stackSize
     */
    public int getStackSize() {
        return stackSize;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

}
