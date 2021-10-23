package alexsocol.patcher

import alexsocol.asjlib.*
import alexsocol.asjlib.command.*
import alexsocol.patcher.asm.ASJHookLoader
import alexsocol.patcher.event.*
import alexsocol.patcher.handler.*
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.*
import cpw.mods.fml.common.registry.GameData
import net.minecraft.block.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraftforge.common.MinecraftForge

@Mod(modid = "asjpatcher", modLanguageAdapter = KotlinAdapter.className)
object PatcherMain {
	
	@Mod.EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		Blocks.melon_stem.setBlockName("melonStem")
		Blocks.piston_head.setBlockName("pistonHead")
		Blocks.piston_extension.setBlockName("pistonExtension")
		Blocks.end_portal.setBlockName("endPortal")
		
		if (!PatcherConfigHandler.addBlocks) return
		
		val noItems = hashSetOf(Blocks.brewing_stand, Blocks.bed, Blocks.nether_wart, Blocks.cauldron, Blocks.flower_pot, Blocks.wheat, Blocks.reeds, Blocks.cake, Blocks.skull, Blocks.piston_head, Blocks.piston_extension, Blocks.lit_redstone_ore, Blocks.powered_repeater, Blocks.pumpkin_stem, Blocks.standing_sign, Blocks.powered_comparator, Blocks.tripwire, Blocks.lit_redstone_lamp, Blocks.melon_stem, Blocks.unlit_redstone_torch, Blocks.unpowered_comparator, Blocks.redstone_wire, Blocks.wall_sign, Blocks.unpowered_repeater, Blocks.iron_door, Blocks.wooden_door)
		if (PatcherConfigHandler.addAir) noItems += Blocks.air
		noItems.forEach { block ->
			GameData.getMain().registerItem(ItemBlock(block), Block.blockRegistry.getNameForObject(block) + "_item", Block.getIdFromBlock(block))
		}
	}
	
	@Mod.EventHandler
	fun init(e: FMLInitializationEvent) {
		PatcherEventHandler.eventForge()
		
		BlockTrapDoor.disableValidation = PatcherConfigHandler.floatingTrapDoors
		
		if (ASJUtilities.isClient)
			PatcherEventHandlerClient.eventForge()
	}
	
	@Mod.EventHandler
	fun onServerStarting(e: FMLServerStartingEvent) {
		e.registerServerCommand(CommandDimTP)
		e.registerServerCommand(CommandExplode)
		e.registerServerCommand(CommandHeal)
		e.registerServerCommand(CommandSchema)
		
		if (!ASJHookLoader.OBF) e.registerServerCommand(CommandResources)
		
		MinecraftForge.EVENT_BUS.post(ServerStartingEvent(e))
	}
	
	@Mod.EventHandler
	fun onServerStarted(e: FMLServerStartedEvent) {
		MinecraftForge.EVENT_BUS.post(ServerStartedEvent(e))
	}
	
	@Mod.EventHandler
	fun onServerStopping(e: FMLServerStoppingEvent) {
		MinecraftForge.EVENT_BUS.post(ServerStoppingEvent(e))
	}
	
	@Mod.EventHandler
	fun onServerStopped(e: FMLServerStoppedEvent) {
		MinecraftForge.EVENT_BUS.post(ServerStoppedEvent(e))
	}
}