package ru.vamig.worldengine.standardcustomgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alfheim.common.world.dim.alfheim.struct.StructureArena;
import alfheim.common.world.dim.alfheim.struct.StructureGovnoClass;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WE_StructureGen implements IWorldGenerator {
	public List<StrList> sttngs = new ArrayList<StrList>();
	
	public void add(StructureGovnoClass str, int notAllowedChXMin, int notAllowedChXMax, int notAllowedChZMin, int notAllowedChZMax, int chXtoMUSTgen, int chZtoMUSTgen, int chance, boolean notAllowing, boolean mustGen) {
		sttngs.add(new StrList(str, notAllowedChXMin, notAllowedChXMax, notAllowedChZMin, notAllowedChZMax, chXtoMUSTgen, chZtoMUSTgen, chance, notAllowing, mustGen));
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for(StrList a : sttngs)
			if(!a.notAllowing || chunkX < a.notAllowedChXMin || chunkX > a.notAllowedChXMax || chunkZ < a.notAllowedChZMin || chunkZ > a.notAllowedChZMax)
				if(rand.nextInt(a.chance) == 0 || a.mustGen && chunkX == a.chXtoMUSTgen && chunkZ == a.chZtoMUSTgen) {
					int x = chunkX * 16 + rand.nextInt(16), z = chunkZ * 16 + rand.nextInt(16);
					a.str.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z);
				}
	}
	
	public class StrList {
		public final StructureGovnoClass str;
		public final int notAllowedChXMin;
		public final int notAllowedChXMax;
		public final int notAllowedChZMin;
		public final int notAllowedChZMax;
		public final int chXtoMUSTgen;
		public final int chZtoMUSTgen;
		public final int chance;
		public final boolean notAllowing;
		public final boolean mustGen;
		
		StrList(StructureGovnoClass str, int notAllowedChXMin, int notAllowedChXMax, int notAllowedChZMin, int notAllowedChZMax, int chXtoMUSTgen, int chZtoMUSTgen, int chance, boolean notAllowing, boolean mustGen) {
			this.str = str;
			this.notAllowedChXMin = notAllowedChXMin;
			this.notAllowedChXMax = notAllowedChXMax;
			this.notAllowedChZMin = notAllowedChZMin;
			this.notAllowedChZMax = notAllowedChZMax;
			this.chXtoMUSTgen = chXtoMUSTgen;
			this.chZtoMUSTgen = chZtoMUSTgen;
			this.chance = chance;
			this.notAllowing = notAllowing;
			this.mustGen = mustGen;
		}
	}
}