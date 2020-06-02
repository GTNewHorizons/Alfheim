package alfheim.common.block

import alexsocol.asjlib.*
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.Hook.ReturnValue
import gloomyfolken.hooklib.asm.ReturnCondition.*
import net.minecraft.block.BlockQuartz
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.block.decor.quartz.BlockSpecialQuartz
import vazkii.botania.common.item.block.ItemBlockSpecialQuartz
import vazkii.botania.common.lib.LibBlockNames

object QuartzExtender {
	
	// ################################ Vanilla block ################################
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "<init>", isMandatory = true)
	fun `BlockQuartz$init`(block: BlockQuartz) {
		BlockQuartz.field_150191_a += arrayOf("lines", "lines", "insert", "smooth")
		BlockQuartz.field_150189_b += arrayOf("insert", "smooth")
		
	}
	
	lateinit var iconInsertNether: IIcon
	lateinit var iconSmoothNether: IIcon
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true)
	fun registerBlockIcons(block: BlockQuartz, reg: IIconRegister) {
		iconInsertNether = reg.registerIcon("${LibResources.PREFIX_MOD}decor/blockInsertNetherQuartz")
		iconSmoothNether = reg.registerIcon("${LibResources.PREFIX_MOD}decor/blockSmoothNetherQuartz")
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ON_NOT_NULL)
	fun getIcon(block: BlockQuartz, side: Int, meta: Int): IIcon? {
		return when (meta) {
			5 -> iconInsertNether
			6 -> iconSmoothNether
			else -> null
		}
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true)
	fun getSubBlocks(block: BlockQuartz, item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		list.add(ItemStack(block, 1, 5))
		list.add(ItemStack(block, 1, 6))
	}
	
	// ################################ Botania block ################################
	
	@JvmStatic
	@Hook(injectOnExit = true, targetMethod = "<init>", isMandatory = true)
	fun `BlockSpecialQuartz$init`(block: BlockSpecialQuartz, type: String) {
		block.iconNames += arrayOf("decor/blockInsert${type}Quartz", "decor/blockSmooth${type}Quartz")
		
		if (type == LibBlockNames.QUARTZ_ELF)
			block.iconNames += arrayOf(
				"decor/elf/ElfQuartzChiseled",
				"decor/elf/ElfQuartzOrnate",
				"decor/elf/ElfQuartzPanel",
				"decor/elf/ElfQuartzRoad"
			)
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, returnCondition = ALWAYS, isMandatory = true)
	fun getNames(block: BlockSpecialQuartz, @ReturnValue res: Array<String>): Array<String> {
		var ret = res + arrayOf(
			"tile.botania:pillar${block.type}Quartz",
			"tile.botania:pillar${block.type}Quartz",
			"tile.alfheim:insert${block.type}Quartz",
			"tile.alfheim:smooth${block.type}Quartz")
		
		if (block.type == LibBlockNames.QUARTZ_ELF)
			ret += arrayOf(
				"tile.alfheim:chiseledElfQuartz",
				"tile.alfheim:ornateElfQuartz",
				"tile.alfheim:panelElfQuartz",
				"tile.alfheim:roadElfQuartz"
			)
		
		return ret
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(returnCondition = ON_NOT_NULL)
	fun getIcon(block: BlockSpecialQuartz, side: Int, meta: Int): IIcon? {
		return if (meta in if (block.type == LibBlockNames.QUARTZ_ELF) 5..10 else 5..6) block.specialQuartzIcons[meta] else null
	}
	
	@SideOnly(Side.CLIENT)
	@JvmStatic
	@Hook(injectOnExit = true)
	fun getSubBlocks(block: BlockSpecialQuartz, item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		(5..6).forEach { list.add(ItemStack(block, 1, it)) }
		
		if (block.type == LibBlockNames.QUARTZ_ELF)
			(7..10).forEach { list.add(ItemStack(block, 1, it)) }
	}
	
	// ################################ Botania item ################################
	
	@JvmStatic
	@Hook(returnCondition = ALWAYS, isMandatory = true)
	fun getUnlocalizedName(item: ItemBlockSpecialQuartz, stack: ItemStack): String? {
		return (item.field_150939_a as BlockSpecialQuartz).names.safeGet(stack.meta)
	}
}