package de.GaMoFu.RaidBosses;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.generator.CraftChunkData;
import org.bukkit.generator.ChunkGenerator;

public class RaidInstanceWorldGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        // // TODO Auto-generated method stub
        // return super.generateChunkData(world, random, x, z, biome);
        ChunkData result = new CraftChunkData(world);
        if (x == 0 && z == 0) {
            result.setBlock(0, 0, 0, Material.BEDROCK);
        }
        return result;
    }

}
