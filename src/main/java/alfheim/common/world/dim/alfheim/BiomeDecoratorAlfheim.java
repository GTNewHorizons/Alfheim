package alfheim.common.world.dim.alfheim;

import java.util.Random;

import alfheim.common.core.registry.AlfheimBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BiomeDecoratorAlfheim {

	public World currentWorld;
	protected Random rand;

	protected int chunkX;
	protected int chunkZ;

	public void decorate(World world, Random random, int chunkX, int chunkZ) {
		this.currentWorld = world;
		this.rand = random;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, rand, chunkX, chunkZ));
		this.decoration();
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, rand, chunkX, chunkZ));
		this.currentWorld = null;
		this.rand = null;
	}

	private void decoration() {

		int i;
		int j;
		int k;

		for (i = 0; this.getGen(EventType.SAND) && i < 3; ++i) {
			int randPosX = this.chunkX + this.rand.nextInt(16) + 8;
			int randPosZ = this.chunkZ + this.rand.nextInt(16) + 8;

			//if(this.currentWorld.getTopSolidOrLiquidBlock(randPosX, randPosZ) < 65)
			new WorldGenSand(AlfheimBlocks.elvenSand, 10).generate(this.currentWorld, this.rand, randPosX,this.currentWorld.getTopSolidOrLiquidBlock(randPosX, randPosZ), randPosZ);

		}

	}

	private boolean getGen(EventType event) {
		return TerrainGen.decorate(this.currentWorld, this.rand, this.chunkX, this.chunkZ, event);
	}
}