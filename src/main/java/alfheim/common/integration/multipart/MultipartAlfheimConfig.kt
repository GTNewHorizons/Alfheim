package alfheim.common.integration.multipart

import alfheim.common.block.AlfheimBlocks
import codechicken.microblock.*
import net.minecraft.block.Block

object MultipartAlfheimConfig {
	
	fun loadConfig() {
		MPHandler.register()
	}
}

object MPHandler {
	
	fun register() {
		registerColoredMultiparts(AlfheimBlocks.irisDirt, 0..15)
		registerMultiparts(AlfheimBlocks.rainbowDirt)
		
		registerColoredMultiparts(AlfheimBlocks.irisWood0, 0..3)
		registerColoredMultiparts(AlfheimBlocks.irisWood1, 0..3)
		registerColoredMultiparts(AlfheimBlocks.irisWood2, 0..3)
		registerColoredMultiparts(AlfheimBlocks.irisWood3, 0..3)
		registerMultiparts(AlfheimBlocks.rainbowWood)
		registerMultiparts(AlfheimBlocks.altWood0, 0..3)
		registerMultiparts(AlfheimBlocks.altWood1, 0..1)
		
		registerColoredMultiparts(AlfheimBlocks.irisPlanks, 0..15)
		registerMultiparts(AlfheimBlocks.rainbowPlanks)
		registerMultiparts(AlfheimBlocks.altPlanks, 0..5)
		
		registerMultiparts(AlfheimBlocks.kindling)
		
		registerMultiparts(AlfheimBlocks.lightningWood)
		registerMultiparts(AlfheimBlocks.lightningPlanks)
		
		registerMultiparts(AlfheimBlocks.netherWood)
		registerMultiparts(AlfheimBlocks.netherPlanks)
		
		registerMultiparts(AlfheimBlocks.sealingWood)
		registerMultiparts(AlfheimBlocks.sealingPlanks)
		
		registerMultiparts(AlfheimBlocks.amplifier)
		
		registerColoredMultiparts(AlfheimBlocks.irisLeaves0, 0..7)
		registerColoredMultiparts(AlfheimBlocks.irisLeaves1, 0..7)
		registerMultiparts(AlfheimBlocks.rainbowLeaves)
		registerMultiparts(AlfheimBlocks.altLeaves, 0..5)
		registerMultiparts(AlfheimBlocks.lightningLeaves)
		registerMultiparts(AlfheimBlocks.netherLeaves)
		registerMultiparts(AlfheimBlocks.sealingLeaves)
		
		registerMultiparts(AlfheimBlocks.shimmerQuartz, 0..2)
	}
	
	fun registerMultiparts(block: Block, meta: Int = 0) {
		registerMultipart(block, meta)
	}
	
	fun registerMultiparts(block: Block, range: IntRange) {
		for (i in range) registerMultiparts(block, i)
	}
	
	fun registerColoredMultiparts(block: Block, meta: Int = 0) {
		registerOverlaidMultipart(block, meta)
	}
	
	fun registerColoredMultiparts(block: Block, range: IntRange) {
		for (i in range) registerColoredMultiparts(block, i)
	}
	
	fun registerMultipart(block: Block, meta: Int) {
		MicroMaterialRegistry.registerMaterial(BlockMicroMaterial(block, meta), block.unlocalizedName + if (meta == 0) "" else "_$meta")
	}
	
	fun registerOverlaidMultipart(block: Block, meta: Int) {
		MicroMaterialRegistry.registerMaterial(ColoredMicroMaterial(block, meta), block.unlocalizedName + if (meta == 0) "" else "_$meta")
	}
}

class ColoredMicroMaterial(val block: Block, val meta: Int): BlockMicroMaterial(block, meta) {
	
	override fun getColour(pass: Int) = block.getRenderColor(meta) shl 8 or 0xFF
}