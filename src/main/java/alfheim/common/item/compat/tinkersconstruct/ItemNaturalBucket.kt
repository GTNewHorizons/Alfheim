package alfheim.common.item.compat.tinkersconstruct

import alexsocol.asjlib.*
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimModule
import alfheim.common.item.ItemMod
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.fluids.BlockFluidFinite

class ItemNaturalBucket: ItemMod("NaturalBucket") {
	
	init {
		maxStackSize = 1
		creativeTab = CreativeTabs.tabMisc
		containerItem = Items.bucket
		setHasSubtypes(true)
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack? {
		val wannabeFull = false
		val position = getMovingObjectPositionFromPlayer(world, player, wannabeFull) ?: return stack
		
		val event = FillBucketEvent(player, stack, world, position)
		if (MinecraftForge.EVENT_BUS.post(event))
			return stack
		
		if (event.getResult() == Event.Result.ALLOW) {
			if (player.capabilities.isCreativeMode)
				return stack
			
			if (--stack.stackSize <= 0)
				return event.result
			
			if (!player.inventory.addItemStackToInventory(event.result))
				player.dropPlayerItemWithRandomChoice(event.result, false)
			
			return stack
		}
		
		if (position.typeOfHit == MovingObjectType.BLOCK) {
			var clickX = position.blockX
			var clickY = position.blockY
			var clickZ = position.blockZ
			
			if (!world.canMineBlock(player, clickX, clickY, clickZ))
				return stack
			
			if (position.sideHit == 0) --clickY
			if (position.sideHit == 1) ++clickY
			if (position.sideHit == 2) --clickZ
			if (position.sideHit == 3) ++clickZ
			if (position.sideHit == 4) --clickX
			if (position.sideHit == 5) ++clickX
			
			if (!player.canPlayerEdit(clickX, clickY, clickZ, position.sideHit, stack))
				return stack
			
			if (tryPlaceContainedLiquid(world, clickX, clickY, clickZ, stack.itemDamage) && !player.capabilities.isCreativeMode)
				return ItemStack(Items.bucket)
			
		}
		
		return stack
	}
	
	fun tryPlaceContainedLiquid(world: World, clickX: Int, clickY: Int, clickZ: Int, type: Int): Boolean {
		if (!world.isAirBlock(clickX, clickY, clickZ) && world.getBlock(clickX, clickY, clickZ).material.isSolid) return false
		
		var metadata = 0
		if (TinkersConstructAlfheimModule.naturalFluidBlocks[type] is BlockFluidFinite) metadata = 7
		world.setBlock(clickX, clickY, clickZ, TinkersConstructAlfheimModule.naturalFluidBlocks[type], metadata, 3)
		
		return true
	}
	
	override fun getSubItems(b: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in icons.indices)
			if (TinkersConstructAlfheimModule.naturalFluidBlocks[i] !== Blocks.flowing_water)
				list.add(ItemStack(b, 1, i))
	}
	
	lateinit var icons: Array<IIcon>
	
	@SideOnly(Side.CLIENT)
	override fun getIconFromDamage(meta: Int) = icons.safeGet(meta)
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		icons = Array(materialNames.size) { reg.registerIcon("tinker:materials/Bucket${materialNames.safeGet(it)}") }
	}
	
	override fun getUnlocalizedName(stack: ItemStack): String = "$unlocalizedName.${materialNames.safeGet(stack.meta)}"
	
	companion object {
		
		val materialNames = TinkersConstructAlfheimModule.naturalFluids.map { it.name.replace(".molten", "").capitalize() }
		
		init {
			MinecraftForge.EVENT_BUS.register(this)
		}
		
		@SubscribeEvent
		fun bucketFill(e: FillBucketEvent) {
			if (e.current.item === Items.bucket && e.target.typeOfHit == MovingObjectType.BLOCK) {
				val hitX = e.target.blockX
				val hitY = e.target.blockY
				val hitZ = e.target.blockZ
				
				if (e.entityPlayer?.canPlayerEdit(hitX, hitY, hitZ, e.target.sideHit, e.current) == false)
					return
				
				val bID = e.world.getBlock(hitX, hitY, hitZ)
				
				if (bID === Blocks.flowing_water) return // not getting water if some material is disabled and set as water
				
				for (id in TinkersConstructAlfheimModule.naturalFluidBlocks.indices) {
					if (bID === TinkersConstructAlfheimModule.naturalFluidBlocks[id]) {
						
						e.world.setBlockToAir(hitX, hitY, hitZ)
						
						if (e.entityPlayer?.capabilities?.isCreativeMode == false) {
							e.setResult(Event.Result.ALLOW)
							e.result = ItemStack(TinkersConstructAlfheimModule.naturalBucket, 1, id)
						}
					}
				}
			}
		}
	}
}