package de.GaMoFu.RaidBosses;

public class CraftingHandler {
    
    private RaidBosses plugin;
    
    public CraftingHandler(RaidBosses plugin) {
        this.plugin = plugin;
        plugin.getServer().resetRecipes();
        
        initRecipes();
    }
    
    private void initRecipes() {
//        this.plugin.getServer().addRecipe(recipe);
    }

}
