package alfheim.common.block.alt

import alexsocol.asjlib.*
import alexsocol.asjlib.render.*
import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.core.handler.CardinalSystem
import alfheim.common.core.helper.IconHelper
import alfheim.common.item.block.ItemUniqueSubtypedBlockMod
import alfheim.common.lexicon.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.*
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.Botania
import vazkii.botania.common.item.ModItems
import java.util.*

class BlockAltLeaves: BlockLeavesMod(), IGlowingLayerBlock {
	
	init {
		setBlockName("altLeaves")
	}
	
	// IDK whether this is good source of glowstone or not
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val stack = player.currentEquippedItem ?: return false
		
		val meta = world.getBlockMetadata(x, y, z)
		
		if (stack.item === ModItems.manaResource && stack.meta == 9 && meta % 8 == yggMeta + 1) {
			var eat = 2
			val sides = BooleanArray(6)
			
			for (dir in ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN || eat <= 0) continue
				
				if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) === this && world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) % 8 == yggMeta + 1) {
					--eat
					sides[dir.ordinal] = true
				}
			}
			
			if (eat > 0) return false
			
			for (dir in ForgeDirection.values()) {
				if (dir == ForgeDirection.UNKNOWN) continue
				if (sides[dir.ordinal]) world.setBlockToAir(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
			}
			
			world.setBlockToAir(x, y, z)
			
			stack.stackSize--
			if (!player.inventory.addItemStackToInventory(ItemStack(Items.glowstone_dust))) {
				player.dropPlayerItemWithRandomChoice(ItemStack(Items.glowstone_dust), true)
			}
			
			if (!world.isRemote && player is EntityPlayerMP) CardinalSystem.KnowledgeSystem.learn(player, CardinalSystem.KnowledgeSystem.Knowledge.GLOWSTONE)
			
			return true
		}
		
		return false
	}
	
	override fun getBlockHardness(world: World, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) % 8 == yggMeta) -1f else super.getBlockHardness(world, x, y, z)
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemUniqueSubtypedBlockMod::class.java, name)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = arrayOf(
			Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, ALT_TYPES[i]) },
			Array(ALT_TYPES.size) { i -> IconHelper.forBlock(reg, this, "${ALT_TYPES[i]}_opaque") }
		)
		
		glowIcon = IconHelper.forBlock(reg, this, "DreamwoodGlow")
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		setGraphicsLevel(mc.gameSettings.fancyGraphics)
		return textures[field_150127_b].safeGet(meta and decayBit().inv())
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = if (meta % 8 == yggMeta) null else if (meta % 8 == yggMeta + 1) AlfheimBlocks.dreamSapling.toItem() else AlfheimBlocks.irisSapling.toItem()
	
	override fun createStackedBlock(meta: Int) = ItemStack(this, 1, meta % 8)
	
	override fun func_150123_b(meta: Int) = if (meta == yggMeta) 0 else if (meta == yggMeta + 1) 100 else 60
	
	override fun quantityDropped(random: Random) = if (random.nextInt(func_150123_b(0)) == 0) 1 else 0
	
	override fun func_150125_e() = ALT_TYPES
	
	override fun getSubBlocks(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in ALT_TYPES.indices)
			list.add(ItemStack(item, 1, i))
	}
	
	override fun decayBit() = 0b1000
	
	override fun canDecay(meta: Int) = if (meta % 8 == yggMeta) false else super.canDecay(meta)
	
	override fun isLeaves(world: IBlockAccess, x: Int, y: Int, z: Int) = if (world.getBlockMetadata(x, y, z) % 8 == yggMeta) false else super.isLeaves(world, x, y, z)
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?): LexiconEntry? {
		val meta = world.getBlockMetadata(x, y, z)
		return when {
			meta % 8 == yggMeta + 1 -> AlfheimLexiconData.worldgen
			meta % 8 == yggMeta     -> null
			else                    -> ShadowFoxLexiconData.irisSapling
		}
	}
	
	override fun getRenderType() = RenderGlowingLayerBlock.glowBlockID
	
	override fun getGlowIcon(side: Int, meta: Int) = if (meta % 8 == 7) glowIcon else null
	
	override fun randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
		if (world.getBlockMetadata(x, y, z) % 8 == 7)
			spawnRandomSpirit(world, x, y, z, rand, 0f, rand.nextFloat() * 0.25f + 0.5f, 1f)
	}
	
	companion object {
		lateinit var textures: Array<Array<IIcon>>
		lateinit var glowIcon: IIcon
	
		val yggMeta = ALT_TYPES.indexOf("Wisdom")
	
		fun spawnRandomSpirit(world: World, x: Int, y: Int, z: Int, rand: Random, r: Float, g: Float, b: Float) {
			if (world.worldTime % 24000 in 13333..22666 && rand.nextInt(512) == 0) {
				val i = Math.random()
				val j = Math.random()
				val k = Math.random()
				val s = Math.random()
				val m = Math.random()
				val n = Math.random()
				val o = Math.random()
				val l = Math.random()
				
				Botania.proxy.setWispFXDistanceLimit(false)
				for (q in 0..4)
					Botania.proxy.wispFX(world, x + i, y + j * 5 + 1, z + k, r, g, b, s.F * 0.25f + 0.1f, m.F * 0.1f - 0.05f, n.F * 0.01F, o.F * 0.1f - 0.05f, l.F * 20f + 5f)
				Botania.proxy.setWispFXDistanceLimit(true)
			}
		}
	}
}
