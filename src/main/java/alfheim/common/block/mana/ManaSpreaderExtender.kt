package alfheim.common.block.mana

import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.helper.IconHelper
import gloomyfolken.hooklib.asm.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.texture.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.render.block.RenderSpreader
import vazkii.botania.client.render.tile.RenderTileSpreader
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.mana.BlockSpreader
import vazkii.botania.common.block.tile.mana.TileSpreader
import vazkii.botania.common.entity.EntityManaBurst

@Suppress("UNUSED_PARAMETER", "NAME_SHADOWING", "unused", "FunctionName")
object ManaSpreaderExtender {
	
	const val MAX_MANA = 1000
	const val ULTRA_MAX_MANA = 6400
	const val UBER_MAX_MANA = 24000
	
	lateinit var icon: IIcon
	
	var staticUber = false
	
	// ######## BlockSpreader
	
	@JvmStatic
	@Hook
	fun registerBlockIcons(spreader: BlockSpreader, reg: IIconRegister) {
		icon = IconHelper.forName(reg, "UberSpreader")
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getIcon(spreader: BlockSpreader, side: Int, meta: Int): IIcon = if (meta == 4) icon else if (meta >= 2) ModBlocks.dreamwood.getIcon(side, 0) else ModBlocks.livingwood.getIcon(side, 0)
	
	// ######## TileSpreader
	
	var hook = false
	
	@JvmStatic
	@Hook
	fun getBurst(tile: TileSpreader, fake: Boolean): EntityManaBurst? {
		if (isUBER_SPREADER(tile)) hook = true
		
		return null
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "<init>")
	fun `BurstProperties$init`(bp: BurstProperties, maxMana: Int, ticksBeforeManaLoss: Int, manaLossPerTick: Float, gravity: Float, motionModifier: Float, color: Int) {
		if (hook) {
			bp.maxMana = 2400
			bp.color = 0xFFD400
			bp.ticksBeforeManaLoss = 180
			bp.manaLossPerTick = 32f
			bp.motionModifier = 3f
			
			hook = false
		}
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getMaxMana(tile: TileSpreader) = if (isUBER_SPREADER(tile)) UBER_MAX_MANA else if (tile.isULTRA_SPREADER) ULTRA_MAX_MANA else MAX_MANA
	
	fun isUBER_SPREADER(tile: TileSpreader) = if (tile.worldObj == null) staticUber else tile.getBlockMetadata() == 4
	
	// ######## RenderSpreader
	
	@JvmStatic
	@Hook
	fun renderInventoryBlock(render: RenderSpreader, block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		staticUber = metadata == 4
	}
	
	// ######## RenderTileSpreader
	
	var rhook = false
	
	@JvmStatic
	@Hook
	fun renderTileEntityAt(render: RenderTileSpreader, tile: TileEntity, d0: Double, d1: Double, d2: Double, ticks: Float) {
		rhook = true
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ON_TRUE)
	fun bindTexture(tm: TextureManager, loc: ResourceLocation?): Boolean {
		if (rhook) {
			rhook = false
			tm.bindTexture(if (ClientProxy.dootDoot) LibResourceLocations.uberSpreaderHalloween else LibResourceLocations.uberSpreader)
			
			return true
		}
		
		return false
	}
}