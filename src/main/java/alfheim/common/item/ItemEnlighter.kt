package alfheim.common.item

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileRainbowManaFlame
import alfheim.client.core.helper.IconHelper
import alfheim.common.item.rod.ItemRodPrismatic
import alexsocol.asjlib.security.InteractionSecurity
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidClassic
import vazkii.botania.api.mana.*

class ItemEnlighter: ItemMod("Enlighter"), IManaUsingItem {
	
	lateinit var iconLit: IIcon
	
	init {
		maxStackSize = 1
	}
	
	override fun registerIcons(reg: IIconRegister) {
		super.registerIcons(reg)
		iconLit = IconHelper.forItem(reg, this, "Lit")
	}
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun getRenderPasses(meta: Int) = meta + 1
	
	override fun getIconFromDamageForRenderPass(meta: Int, pass: Int) = if (meta == 1 && pass == 1) iconLit else itemIcon!!
	
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) = if (stack.meta == 1 && pass == 1) ItemIridescent.rainbowColor() else 0xFFFFFF
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.isSneaking)
			stack.meta = 1 - stack.meta
		
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slot: Int, inHand: Boolean) {
		if (!usesMana(stack)) return
		
		if (entity is EntityPlayer) {
			val x = entity.posX.mfloor()
			val y = entity.posY.mfloor()
			val z = entity.posZ.mfloor()
			
			if (!InteractionSecurity.canDoSomethingHere(entity, x, y, z, world)) return
			
			if (world.getLightBrightness(x, y, z) < 0.25F) {
				val block = world.getBlock(x, y, z)
				val below = world.getBlock(x, y - 1, z)
				
				if (below.isAir(world, x, y, z) || !block.isAir(world, x, y, z)) return
				if (below is BlockFluidClassic || below is BlockFluidClassic) return
				if (block.material == Material.water || below.material == Material.water) return
				if (block.material == Material.lava || below.material == Material.lava) return
				
				val toPlace = ItemStack(AlfheimBlocks.rainbowFlame)
				if (ManaItemHandler.requestManaExactForTool(stack, entity, ItemRodPrismatic.COST, false)) {
					toPlace.tryPlaceItemIntoWorld(entity, world, x, y, z, 1, 0f, 0f, 0f)
					if (toPlace.stackSize == 0) {
						world.playSoundEffect(x.D + 0.5, y.D + 0.5, z.D + 0.5, "fire.ignite", 0.3F, Math.random().F * 0.4F + 0.8F)
						ManaItemHandler.requestManaExactForTool(stack, entity, ItemRodPrismatic.COST, true)
						val tile = world.getTileEntity(x, y, z)
						if (tile is TileRainbowManaFlame)
							tile.invisible = true
					}
				}
			}
		}
	}
	
	override fun usesMana(stack: ItemStack) = stack.meta == 1
}
