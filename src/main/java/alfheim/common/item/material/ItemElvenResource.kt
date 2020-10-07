package alfheim.common.item.material

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.client.core.helper.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.BlockRainbowGrass
import alfheim.common.item.*
import alfheim.common.item.material.ElvenResourcesMetas.ElvenWeed
import alfheim.common.item.material.ElvenResourcesMetas.InfusedDreamwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimEssence
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodCoal
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodSplinters
import alfheim.common.item.material.ElvenResourcesMetas.NetherwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.RainbowDust
import alfheim.common.item.material.ElvenResourcesMetas.RainbowPetal
import alfheim.common.item.material.ElvenResourcesMetas.RainbowQuartz
import alfheim.common.item.material.ElvenResourcesMetas.ThunderwoodSplinters
import alfheim.common.item.material.ElvenResourcesMetas.ThunderwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.displayBlackList
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.recipe.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color

class ItemElvenResource: ItemMod("ElvenItems"), IElvenItem, IFlowerComponent, IFuelHandler/*, ILensEffect*/ {
	
	val texture = arrayOfNulls<IIcon>(subItems.size)
	
	init {
		setHasSubtypes(true)
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
		
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun getRenderPasses(meta: Int) =
		when (meta) {
			ElvenWeed -> 2
			else      -> 1
		}
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun isElvenItem(stack: ItemStack) = stack.meta == ElvenResourcesMetas.InterdimensionalGatewayCore
	
	fun isInterpolated(meta: Int) = meta == ThunderwoodTwig || meta == NetherwoodCoal || meta == RainbowQuartz
	
	fun isFlowerComponent(meta: Int) = meta == NetherwoodCoal || meta == RainbowPetal
	
	override fun canFit(stack: ItemStack, inventory: IInventory) = isFlowerComponent(stack.meta)
	
	override fun getParticleColor(stack: ItemStack): Int {
		return when (stack.meta) {
			NetherwoodCoal -> 0x6B2406
			RainbowPetal   -> ItemIridescent.rainbowColor()
			else           -> 0xFFFFFF
		}
	}
	
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) =
		if (stack.meta == ElvenWeed && pass == 1)
			Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360F, 0.25F, 1F)
		else if (stack.meta == RainbowPetal || stack.meta == RainbowDust)
			ItemIridescent.rainbowColor()
		else
			super.getColorFromItemStack(stack, pass)
	
	override fun registerIcons(reg: IIconRegister) {
		for (i in subItems.indices)
			if (!isInterpolated(i))
				texture[i] = IconHelper.forName(reg, subItems[i], "materials")
		
		amulet = reg.registerIcon(ModInfo.MODID + ":misc/amulet")
		candy = IconHelper.forName(reg, "CandyCane", "materials")
		flugel = reg.registerIcon(ModInfo.MODID + ":misc/flugelBack")
		harp = reg.registerIcon(ModInfo.MODID + ":misc/harp")
		mine = reg.registerIcon(ModInfo.MODID + ":misc/mine")
		wind = reg.registerIcon(ModInfo.MODID + ":misc/wind")
		wing = reg.registerIcon(ModInfo.MODID + ":misc/wing")
		
		weed1 = IconHelper.forName(reg, "materials/${subItems[ElvenWeed]}1")
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1)
			for (i in subItems.indices)
				if (isInterpolated(i))
					texture[i] = InterpolatedIconHelper.forName(event.map, subItems[i], "materials")
	}
	
	override fun getIcon(stack: ItemStack, pass: Int) =
		if (stack.meta == ElvenWeed && pass == 1)
			weed1
		else if (AlfheimCore.jingleTheBells && stack.meta == InfusedDreamwoodTwig)
			candy
		else
			texture.safeGet(stack.meta)!!
	
	override fun getUnlocalizedName(stack: ItemStack) =
		if (AlfheimCore.jingleTheBells && stack.meta == InfusedDreamwoodTwig)
			"item.InfusedCandy"
		else
			"item.${subItems.safeGet(stack.meta)}"
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in subItems.indices)
			if (i !in displayBlackList)
				list.add(ItemStack(item, 1, i))
	}
	
	override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, target: Entity): Boolean {
		return if (stack.meta == ElvenResourcesMetas.DasRheingold && target is EntityPlayer)
			ItemNBTHelper.setString(stack, "nick", target.commandSenderName).let { true }
		else
			super.onLeftClickEntity(stack, player, target)
	}
	
	val ids = arrayOf(Potion.moveSpeed.id, Potion.regeneration.id, Potion.jump.id, Potion.hunger.id, Potion.confusion.id)
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (stack.meta != ElvenWeed) return stack
		
		for (i in ids)
			player.addPotionEffect(PotionEffect(i, 600))
		
		--stack.stackSize
		return stack
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer?, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean {
		val block = world.getBlock(x, y, z)
		// Fabulous manapool
		if (block == ModBlocks.pool && world.getBlockMetadata(x, y, z) == 0 && stack.meta == RainbowDust) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2)
			stack.stackSize--
			return true
		} else
		// Rainbow flower
			if (block == ModBlocks.flower && stack.meta == RainbowDust) {
				world.setBlock(x, y, z, AlfheimBlocks.rainbowGrass, BlockRainbowGrass.FLOWER, 3)
				for (i in 0..40) {
					val color = Color.getHSBColor(Math.random().F + 1f / 2f, 1f, 1f)
					Botania.proxy.wispFX(world,
										 x.D + Math.random(), y.D + Math.random(), z.D + Math.random(),
										 color.red / 255f, color.green / 255f, color.blue / 255f,
										 0.5f, 0f, 0.125f, 0f)
				}
				world.playSoundEffect(x.D, y.D, z.D, "botania:enchanterEnchant", 1f, 1f)
				stack.stackSize--
				return true
			} else
			// Burying petal
				if (side == 1 && AlfheimBlocks.rainbowGrass.canBlockStay(world, x, y + 1, z) && stack.meta == RainbowPetal) {
					world.setBlock(x, y + 1, z, AlfheimBlocks.rainbowGrass, BlockRainbowGrass.BURIED, 3)
					stack.stackSize--
					return true
				}
		return false
	}
	
	override fun getBurnTime(fuel: ItemStack): Int {
		if (fuel.item === AlfheimItems.elvenResource) {
			return when (fuel.meta) {
				InfusedDreamwoodTwig, ThunderwoodTwig     -> 600 // 2
				NetherwoodTwig                            -> 4000 // 20
				MuspelheimEssence                         -> 12800 // 64
				NetherwoodSplinters, ThunderwoodSplinters -> 100 // 0.5
				NetherwoodCoal                            -> 2400 // 12
				else                                      -> 0
			}
		}
		return 0
	}
	
	companion object {
		
		val subItems = arrayOf("InterdimensionalGatewayCore", "ManaInfusionCore", "DasRheingold", "ElvoriumIngot", "MauftriumIngot", "MuspelheimPowerIngot", "NiflheimPowerIngot", "ElvoriumNugget", "MauftriumNugget", "MuspelheimEssence", "NiflheimEssence", "RainbowQuartz", "RainbowPetal", "RainbowDust", "IffesalDust", "PrimalRune", "MuspelheimRune", "NiflheimRune", "InfusedDreamwoodTwig", "ThunderwoodTwig", "NetherwoodTwig", "ThunderwoodSplinters", "NetherwoodSplinters", "NetherwoodCoal", "ElvenWeed", "Jug", "GrapeLeaf", "FenrirFur", "JormungandScale")
		
		lateinit var amulet: IIcon
		lateinit var candy: IIcon
		lateinit var flugel: IIcon
		lateinit var harp: IIcon
		lateinit var mine: IIcon
		lateinit var wind: IIcon
		lateinit var wing: IIcon
		
		lateinit var weed1: IIcon
	}
}

object ElvenResourcesMetas {
	
	val InterdimensionalGatewayCore: Int
	val ManaInfusionCore: Int
	val DasRheingold: Int
	val ElvoriumIngot: Int
	val MauftriumIngot: Int
	val MuspelheimPowerIngot: Int
	val NiflheimPowerIngot: Int
	val ElvoriumNugget: Int
	val MauftriumNugget: Int
	val MuspelheimEssence: Int
	val NiflheimEssence: Int
	val RainbowQuartz: Int
	val RainbowPetal: Int
	val RainbowDust: Int
	val IffesalDust: Int
	val PrimalRune: Int
	val MuspelheimRune: Int
	val NiflheimRune: Int
	val InfusedDreamwoodTwig: Int
	val ThunderwoodTwig: Int
	val NetherwoodTwig: Int
	val ThunderwoodSplinters: Int
	val NetherwoodSplinters: Int
	val NetherwoodCoal: Int
	val ElvenWeed: Int
	val Jug: Int
	val GrapeLeaf: Int
	val FenrirFur: Int
	val JormungandScale: Int
	
	val displayBlackList: Array<Int>
	
	init {
		val items = ItemElvenResource.subItems
		InterdimensionalGatewayCore = items.indexOf("InterdimensionalGatewayCore")
		ManaInfusionCore = items.indexOf("ManaInfusionCore")
		DasRheingold = items.indexOf("DasRheingold")
		ElvoriumIngot = items.indexOf("ElvoriumIngot")
		MauftriumIngot = items.indexOf("MauftriumIngot")
		MuspelheimPowerIngot = items.indexOf("MuspelheimPowerIngot")
		NiflheimPowerIngot = items.indexOf("NiflheimPowerIngot")
		ElvoriumNugget = items.indexOf("ElvoriumNugget")
		MauftriumNugget = items.indexOf("MauftriumNugget")
		MuspelheimEssence = items.indexOf("MuspelheimEssence")
		NiflheimEssence = items.indexOf("NiflheimEssence")
		RainbowQuartz = items.indexOf("RainbowQuartz")
		RainbowPetal = items.indexOf("RainbowPetal")
		RainbowDust = items.indexOf("RainbowDust")
		IffesalDust = items.indexOf("IffesalDust")
		PrimalRune = items.indexOf("PrimalRune")
		MuspelheimRune = items.indexOf("MuspelheimRune")
		NiflheimRune = items.indexOf("NiflheimRune")
		InfusedDreamwoodTwig = items.indexOf("InfusedDreamwoodTwig")
		ThunderwoodTwig = items.indexOf("ThunderwoodTwig")
		NetherwoodTwig = items.indexOf("NetherwoodTwig")
		ThunderwoodSplinters = items.indexOf("ThunderwoodSplinters")
		NetherwoodSplinters = items.indexOf("NetherwoodSplinters")
		NetherwoodCoal = items.indexOf("NetherwoodCoal")
		ElvenWeed = items.indexOf("ElvenWeed")
		Jug = items.indexOf("Jug")
		GrapeLeaf = items.indexOf("GrapeLeaf")
		FenrirFur = items.indexOf("FenrirFur")
		JormungandScale = items.indexOf("JormungandScale")
		
		displayBlackList = arrayOf(ElvenWeed)
	}
}