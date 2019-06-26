package alfheim.common.block

import alexsocol.asjlib.extendables.MaterialPublic
import alfheim.api.*
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.item.block.ItemBlockAnomaly
import alfheim.common.lexicon.AlfheimLexiconData
import net.minecraft.block.*
import net.minecraft.block.material.MapColor
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import java.util.*

class BlockAnomaly: BlockContainer(anomaly), ILexiconable {
	init {
		setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f)
		setBlockName("Anomaly")
		setBlockTextureName(ModInfo.MODID + ":ManaInfuserTopDark") // why not :)
		setBlockUnbreakable()
		setLightLevel(1f)
		setLightOpacity(0)
		setResistance(java.lang.Float.MAX_VALUE / 3.0f)
		setStepSound(Block.soundTypeCloth)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return (world.getTileEntity(x, y, z) as TileAnomaly).onActivated(player!!.currentEquippedItem, player, world, x, y, z)
	}
	
	override fun getCollisionBoundingBoxFromPool(p_149668_1_: World?, p_149668_2_: Int, p_149668_3_: Int, p_149668_4_: Int): AxisAlignedBB? {
		return null
	}
	
	override fun getSubBlocks(block: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (name in AlfheimAPI.anomalies.keys)
			list.add(ItemBlockAnomaly.ofType(name))
	}
	
	override fun getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int, player: EntityPlayer?): ItemStack {
		val anomaly = ItemStack(AlfheimBlocks.anomaly)
		initNBT(anomaly)
		(world.getTileEntity(x, y, z) as TileAnomaly).writeCustomNBT(getNBT(anomaly))
		return anomaly
	}
	
	override fun createNewTileEntity(world: World, meta: Int): TileEntity {
		return TileAnomaly()
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		iconUndefined = reg.registerIcon(ModInfo.MODID + ":undefined")
	}
	
	override fun isOpaqueCube(): Boolean {
		return false
	}
	
	override fun renderAsNormalBlock(): Boolean {
		return false
	}
	
	override fun getRenderType(): Int {
		return -1
	}
	
	override fun getItemDropped(meta: Int, rand: Random?, luck: Int): Item? {
		return null
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack): LexiconEntry {
		return AlfheimLexiconData.anomaly
	}
	
	companion object {
		
		val anomaly = MaterialPublic(MapColor.airColor).setGrass().setNotOpaque().setImmovableMobility()
		lateinit var iconUndefined: IIcon
	}
}