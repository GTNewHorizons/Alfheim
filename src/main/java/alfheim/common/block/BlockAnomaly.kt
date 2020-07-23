package alfheim.common.block

import alexsocol.asjlib.extendables.MaterialPublic
import alfheim.api.*
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemBlockAnomaly
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import java.util.*

class BlockAnomaly: BlockContainerMod(anomaly), ILexiconable {
	
	init {
		setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f)
		setBlockName("Anomaly")
		setBlockUnbreakable()
		setCreativeTab(AlfheimTab)
		setLightLevel(1f)
		setLightOpacity(0)
		setResistance(java.lang.Float.MAX_VALUE / 3f)
		setStepSound(Block.soundTypeCloth)
	}
	
	override fun shouldRegisterInNameSet() = false
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockAnomaly::class.java, name)
		return super.setBlockName(name)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		return (world.getTileEntity(x, y, z) as TileAnomaly).onActivated(player!!.currentEquippedItem, player, world, x, y, z)
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
	
	override fun registerBlockIcons(reg: IIconRegister) {
		iconUndefined = reg.registerIcon(ModInfo.MODID + ":undefined")
		blockIcon = iconUndefined
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileAnomaly()
	override fun getCollisionBoundingBoxFromPool(p_149668_1_: World?, p_149668_2_: Int, p_149668_3_: Int, p_149668_4_: Int) = null
	override fun isOpaqueCube() = false
	override fun renderAsNormalBlock() = false
	override fun getRenderType() = -1
	override fun getItemDropped(meta: Int, rand: Random?, luck: Int) = null
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, lexicon: ItemStack) = AlfheimLexiconData.anomaly
	
	companion object {
		val anomaly = MaterialPublic(MapColor.airColor).setBlocksLight().setNotOpaque().setImmovableMobility()
		lateinit var iconUndefined: IIcon
	}
}