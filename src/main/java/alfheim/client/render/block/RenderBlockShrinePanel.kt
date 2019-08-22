package alfheim.client.render.block

import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.BlockPaneMeta
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

class RenderBlockShrinePanel : ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block?, metadata: Int, modelId: Int, renderer: RenderBlocks?) = Unit
	
	override fun renderWorldBlock(world: IBlockAccess?, x: Int, y: Int, z: Int, block: Block?, modelId: Int, renderer: RenderBlocks?) =
		(world != null && block is BlockPaneMeta && renderer != null) && renderBlockStainedGlassPane(world, block, x, y, z, renderer)
	
	
	// unholly code from RenderBlocks#renderBlockStainedGlassPane
	fun renderBlockStainedGlassPane(world: IBlockAccess, block: BlockPaneMeta, x: Int, y: Int, z: Int, renderer: RenderBlocks): Boolean {
		val tessellator = Tessellator.instance
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z))
		val i1 = block.colorMultiplier(world, x, y, z)
		var f = (i1 shr 16 and 255).toFloat() / 255.0f
		var f1 = (i1 shr 8 and 255).toFloat() / 255.0f
		var f2 = (i1 and 255).toFloat() / 255.0f
		
		if (EntityRenderer.anaglyphEnable) {
			val f3 = (f * 30.0f + f1 * 59.0f + f2 * 11.0f) / 100.0f
			val f4 = (f * 30.0f + f1 * 70.0f) / 100.0f
			val f5 = (f * 30.0f + f2 * 70.0f) / 100.0f
			f = f3
			f1 = f4
			f2 = f5
		}
		
		tessellator.setColorOpaque_F(f, f1, f2)
		val iicon: IIcon
		val iicon1: IIcon
		
		if (renderer.hasOverrideBlockTexture()) {
			iicon = renderer.overrideBlockTexture
			iicon1 = renderer.overrideBlockTexture
		} else {
			val j1 = world.getBlockMetadata(x, y, z)
			iicon = getBlockIconFromSideAndMetadata(block, 0, j1)
			iicon1 = block.getTopIcon(j1)
		}
		
		val d22 = iicon.minU.toDouble()
		val d0 = iicon.getInterpolatedU(7.0).toDouble()
		val d1 = iicon.getInterpolatedU(9.0).toDouble()
		val d2 = iicon.maxU.toDouble()
		val d3 = iicon.minV.toDouble()
		val d4 = iicon.maxV.toDouble()
		val d5 = iicon1.getInterpolatedU(7.0).toDouble()
		val d6 = iicon1.getInterpolatedU(9.0).toDouble()
		val d7 = iicon1.minV.toDouble()
		val d8 = iicon1.maxV.toDouble()
		val d9 = iicon1.getInterpolatedV(7.0).toDouble()
		val d10 = iicon1.getInterpolatedV(9.0).toDouble()
		val d11 = x.toDouble()
		val d12 = (x + 1).toDouble()
		val d13 = z.toDouble()
		val d14 = (z + 1).toDouble()
		val d15 = x.toDouble() + 0.5 - 0.0625
		val d16 = x.toDouble() + 0.5 + 0.0625
		val d17 = z.toDouble() + 0.5 - 0.0625
		val d18 = z.toDouble() + 0.5 + 0.0625
		val flag = block.canPaneConnectTo(world, x, y, z - 1, ForgeDirection.NORTH)
		val flag1 = block.canPaneConnectTo(world, x, y, z + 1, ForgeDirection.SOUTH)
		val flag2 = block.canPaneConnectTo(world, x - 1, y, z, ForgeDirection.WEST)
		val flag3 = block.canPaneConnectTo(world, x + 1, y, z, ForgeDirection.EAST)
		val flag4 = !flag && !flag1 && !flag2 && !flag3
		
		if (!flag2 && !flag4) {
			if (!flag && !flag1) {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d1, d3)
			}
		} else if (flag2 && flag3) {
			if (!flag) {
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d2, d3)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d2, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d22, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d22, d3)
			} else {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d22, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d22, d3)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d2, d3)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d1, d3)
			}
			
			if (!flag1) {
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d22, d3)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d22, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d2, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d2, d3)
			} else {
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d22, d3)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d0, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d2, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d2, d3)
			}
			
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d6, d7)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d6, d8)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d5, d8)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d5, d7)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d5, d8)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d5, d7)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d6, d7)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d6, d8)
		} else {
			if (!flag && !flag4) {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d1, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d1, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d22, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d22, d3)
			} else {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d22, d4)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d22, d3)
			}
			
			if (!flag1 && !flag4) {
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d22, d3)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d22, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
			} else {
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d22, d3)
				tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d0, d3)
			}
			
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d6, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d6, d9)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d5, d9)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d5, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d5, d9)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d5, d7)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d6, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d6, d9)
		}
		
		if ((flag3 || flag4) && !flag2) {
			if (!flag1 && !flag4) {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d0, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d2, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d2, d3)
			} else {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d2, d4)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d2, d3)
			}
			
			if (!flag && !flag4) {
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d2, d3)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d2, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
			} else {
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d2, d3)
				tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d1, d3)
			}
			
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d6, d10)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d6, d7)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d5, d7)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d5, d10)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d5, d8)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d5, d10)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d6, d10)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d6, d8)
		} else if (!flag3 && !flag && !flag1) {
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d0, d3)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d0, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d1, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d1, d3)
		}
		
		if (!flag && !flag4) {
			if (!flag3 && !flag2) {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d1, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d1, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
			}
		} else if (flag && flag1) {
			if (!flag2) {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d22, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d2, d3)
			} else {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d22, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d1, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d2, d3)
			}
			
			if (!flag3) {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d2, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d22, d3)
			} else {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d22, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d2, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
			}
			
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d6, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d5, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d5, d8)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d6, d8)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d5, d7)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d6, d7)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d6, d8)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d5, d8)
		} else {
			if (!flag2 && !flag4) {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d22, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d1, d3)
			} else {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d22, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
			}
			
			if (!flag3 && !flag4) {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d22, d3)
			} else {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d22, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d22, d3)
			}
			
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d6, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d5, d7)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d5, d9)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d6, d9)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d5, d7)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d6, d7)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d6, d9)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d5, d9)
		}
		
		if ((flag1 || flag4) && !flag) {
			if (!flag2 && !flag4) {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d0, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d2, d3)
			} else {
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d1, d3)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d2, d3)
			}
			
			if (!flag3 && !flag4) {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d2, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d0, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d0, d3)
			} else {
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d2, d3)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d2, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
				tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
			}
			
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d6, d10)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d5, d10)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d5, d8)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d6, d8)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d5, d10)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d6, d10)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d6, d8)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d5, d8)
		} else if (!flag1 && !flag3 && !flag2) {
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d0, d3)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d0, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d1, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d1, d3)
		}
		
		tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d17, d6, d9)
		tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d17, d5, d9)
		tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d18, d5, d10)
		tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d18, d6, d10)
		tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d17, d5, d9)
		tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d17, d6, d9)
		tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d18, d6, d10)
		tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d18, d5, d10)
		
		if (flag4) {
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d17, d0, d3)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d17, d0, d4)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.001, d18, d1, d4)
			tessellator.addVertexWithUV(d11, y.toDouble() + 0.999, d18, d1, d3)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d18, d0, d3)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d18, d0, d4)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.001, d17, d1, d4)
			tessellator.addVertexWithUV(d12, y.toDouble() + 0.999, d17, d1, d3)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d13, d1, d3)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d13, d1, d4)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d13, d0, d4)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d13, d0, d3)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.999, d14, d0, d3)
			tessellator.addVertexWithUV(d15, y.toDouble() + 0.001, d14, d0, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.001, d14, d1, d4)
			tessellator.addVertexWithUV(d16, y.toDouble() + 0.999, d14, d1, d3)
		}
		
		return true
	}
	
	fun getBlockIconFromSideAndMetadata(block: Block, meta: Int, side: Int): IIcon =
		getIconSafe(block.getIcon(meta, side))
	
	fun getIconSafe(icon: IIcon?): IIcon =
		icon ?: (Minecraft.getMinecraft().textureManager.getTexture(TextureMap.locationBlocksTexture) as TextureMap).getAtlasSprite("missingno")
	
	override fun shouldRender3DInInventory(modelId: Int) = false
	
	override fun getRenderId() = LibRenderIDs.idShrinePanel
}