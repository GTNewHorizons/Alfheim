package alfheim.common.integration.multipart

import alfheim.common.block.ShadowFoxBlocks
import codechicken.microblock.*
import net.minecraft.block.Block

object MultipartAlfheimConfig {
    fun loadConfig() {
        MPHandler.register()
    }
}

object MPHandler {

    fun register() {
        registerColoredMultiparts(ShadowFoxBlocks.coloredDirtBlock, 0..15)
        registerMultiparts(ShadowFoxBlocks.rainbowDirtBlock)
    
        registerColoredMultiparts(ShadowFoxBlocks.irisWood0, 0..3)
        registerColoredMultiparts(ShadowFoxBlocks.irisWood1, 0..3)
        registerColoredMultiparts(ShadowFoxBlocks.irisWood2, 0..3)
        registerColoredMultiparts(ShadowFoxBlocks.irisWood3, 0..3)
        registerMultiparts(ShadowFoxBlocks.rainbowWood)
        registerMultiparts(ShadowFoxBlocks.altWood0, 0..3)
        registerMultiparts(ShadowFoxBlocks.altWood1, 0..1)
    
        registerColoredMultiparts(ShadowFoxBlocks.coloredPlanks, 0..15)
        registerMultiparts(ShadowFoxBlocks.rainbowPlanks)
        registerMultiparts(ShadowFoxBlocks.altPlanks, 0..5)
    
        registerMultiparts(ShadowFoxBlocks.kindling)
    
        registerMultiparts(ShadowFoxBlocks.lightningWood)
        registerMultiparts(ShadowFoxBlocks.lightningPlanks)
    
        registerMultiparts(ShadowFoxBlocks.netherWood)
        registerMultiparts(ShadowFoxBlocks.netherPlanks)
    
        registerMultiparts(ShadowFoxBlocks.sealingWood)
        registerMultiparts(ShadowFoxBlocks.sealingPlanks)
    
        registerMultiparts(ShadowFoxBlocks.amp)
    
        registerColoredMultiparts(ShadowFoxBlocks.irisLeaves0, 0..7)
        registerColoredMultiparts(ShadowFoxBlocks.irisLeaves1, 0..7)
        registerMultiparts(ShadowFoxBlocks.rainbowLeaves)
        registerMultiparts(ShadowFoxBlocks.altLeaves, 0..5)
        registerMultiparts(ShadowFoxBlocks.lightningLeaves)
        registerMultiparts(ShadowFoxBlocks.netherLeaves)
        registerMultiparts(ShadowFoxBlocks.sealingLeaves)
    
        registerMultiparts(ShadowFoxBlocks.shimmerQuartz, 0..2)
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
        MicroMaterialRegistry.registerMaterial(BlockMicroMaterial(block, meta), block.unlocalizedName + if (meta == 0) "" else "_" + meta)
    }

    fun registerOverlaidMultipart(block: Block, meta: Int) {
        MicroMaterialRegistry.registerMaterial(ColoredMicroMaterial(block, meta), block.unlocalizedName + if (meta == 0) "" else "_" + meta)
    }
}

class ColoredMicroMaterial(val block: Block, val meta: Int) : BlockMicroMaterial(block, meta) {
    override fun getColour(pass: Int) = block.getRenderColor(meta) shl 8 or 0xFF
}