package ru.vamig.worldengine.standardcustomgen;

import net.minecraft.world.World;

import java.util.Random;

public abstract class StructureBaseClass {
	
	public abstract boolean generate(World world, Random rand, int x, int y, int z);
}
