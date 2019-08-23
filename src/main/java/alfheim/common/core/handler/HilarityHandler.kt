package alfheim.common.core.handler

import alfheim.api.ModInfo
import alfheim.common.block.tile.TileItemDisplay
import alfheim.common.crafting.recipe.ShadowFoxRecipes
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.item.ModItems
import java.util.*

/**
 * @author WireSegal
 * Created at 6:11 PM on 2/4/16.
 */
class HilarityHandler {

    companion object {
        var instance = HilarityHandler()

        fun register() {
            MinecraftForge.EVENT_BUS.register(instance)
        }
    }

    private val handlers: ArrayList<CraftHandler> = ArrayList()
    
    private fun registerHandler(playerName: String, cheatyString: String,
                                gender: String, chatColor: EnumChatFormatting,
                                neededItems: List<ItemStack>, normalString: String,
                                resourceItem: ItemStack, outputItem: ItemStack) {
        handlers.add(CraftHandler(playerName, cheatyString,
                                                                              gender, chatColor,
                                                                              neededItems, normalString,
                                                                              resourceItem, outputItem))
    }

    val itemsRequiredWire = arrayListOf(                                                    // + Elementium Axe (in hand)
        ItemStack(ModItems.dice),                                                           //   Dice of Fate              Chaos
        ItemStack(ModItems.manaResource, 1, 5),                                             //   Gaia Spirit               Divinity
        ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ThunderwoodSplinters), //   Thunderous Splinters      Lightning
        ShadowFoxRecipes.skullStack("Tristaric"),                                           //   Tris's head               Humanity
        ItemStack(ModItems.rainbowRod, 1, OreDictionary.WILDCARD_VALUE),                    //   The Rod of the Bifrost    Order
        ItemStack(ModItems.manaResource, 1, 4)                                              //   Terrasteel                Earth
    )

    val itemsRequiredTris = arrayListOf(                                                    // + Elementium Sword (in hand)
        ItemStack(ModItems.dice),                                                           //   Dice of Fate              Chaos
        ItemStack(ModItems.manaResource, 1, 5),                                             //   Gaia Spirit               Divinity
        ItemStack(ModItems.rune, 1, 13),                                                    //   Rune of Wrath             Lightning
        ShadowFoxRecipes.skullStack("yrsegal"),                                             //   Wire's head               Humanity
        ItemStack(ModItems.laputaShard, 1, OreDictionary.WILDCARD_VALUE),                   //   The Shard of Laputa       Order
        ItemStack(ModItems.dirtRod)                                                         //   The Rod of the Lands      Earth
    )

    init {
        registerHandler("yrsegal", "i claim the blade of chaos!", "Male", EnumChatFormatting.GOLD,
                itemsRequiredWire, "i awaken the ancients within all of you! from my soul's fire the world burns anew!",
                ItemStack(ModItems.elementiumAxe, 1, OreDictionary.WILDCARD_VALUE), ItemStack(AlfheimItems.wireAxe))
        registerHandler("Tristaric", "i claim the blade of order!", "Female", EnumChatFormatting.LIGHT_PURPLE,
                itemsRequiredTris, "my inward eye sees the depths of my soul! i accept both sides, and reject my downfall!",
                ItemStack(ModItems.elementiumSword, 1, OreDictionary.WILDCARD_VALUE), ItemStack(AlfheimItems.trisDagger))
    }


    private class CraftHandler(val playerName: String, val cheatyString: String,
                               val gender: String, val chatColor: EnumChatFormatting,
                               val neededItems: List<ItemStack>, val normalString: String,
                               val resourceItem: ItemStack, val outputItem: ItemStack) {
        
        fun execute(e: ServerChatEvent): Boolean {
            val msg = e.message.toLowerCase().trim()
            val player = e.player

            if (player.commandSenderName == playerName && msg == cheatyString) {
                if (replaceItemInHand(player, resourceItem, outputItem)) {
                    e.component.chatStyle.color = chatColor
                    player.worldObj.playSoundAtEntity(player, "ambient.weather.thunder", 100.0f, 0.8f + player.worldObj.rand.nextFloat() * 0.2f)
                    return true
                }
            } else if (msg == normalString) {
                val items = getInfusionPlatforms(player.worldObj, MathHelper.floor_double(player.posX),  MathHelper.floor_double(player.posY),  MathHelper.floor_double(player.posZ))

                val itemsMissing = ArrayList(neededItems)
                for (itemPair in items) {
                    val item = itemPair.stack
                    for (itemNeeded in neededItems) {
                        if (itemNeeded !in itemsMissing) continue
                        if (itemNeeded.item != item.item) continue
                        if (itemNeeded.itemDamage != item.itemDamage && itemNeeded.itemDamage != OreDictionary.WILDCARD_VALUE) continue
                        if (itemNeeded.hasTagCompound() && itemNeeded.tagCompound.getString("SkullOwner") != item.tagCompound.getString("SkullOwner")) continue

                        itemsMissing.remove(itemNeeded)
                        itemPair.flag = true
                    }
                }
                if (itemsMissing.isEmpty()) {
                    if (replaceItemInHand(player, resourceItem, outputItem)) {
                        e.component.chatStyle.color = chatColor
                        for (itemPair in items)
                            if (itemPair.flag) {
                                val te = itemPair.pos.getTileAt(player.worldObj, MathHelper.floor_double(player.posX),  MathHelper.floor_double(player.posY),  MathHelper.floor_double(player.posZ))
                                if (te is TileItemDisplay)
                                    te.setInventorySlotContents(0, null)
                            }
                        player.worldObj.playSoundAtEntity(player, "botania:enchanterEnchant", 1f, 1f)
                        return true
                    }
                }
            } else if (msg == cheatyString) {
                val chat = ChatComponentText(StatCollector.translateToLocal("misc.${ModInfo.MODID}.youAreNotTheChosenOne$gender"))
                chat.chatStyle.color = chatColor
                player.addChatMessage(chat)
                e.isCanceled = true
                return true
            }
            return false
        }

        private class Pos(val x: Int, val y: Int, val z: Int) {
            fun getTileAt(world: World, x: Int, y: Int, z: Int): TileEntity? = world.getTileEntity(x + this.x, y + this.y, z + this.z)
        }

        private class PosPair(val pos: Pos, val stack: ItemStack) {
            var flag = false
        }

        private val platformPositions = arrayOf(
            Pos(2, 0, 2),
            Pos(-2, 0, 2),
            Pos(-2, 0, -2),
            Pos(2, 0, -2),
            Pos(4, 0, 0),
            Pos(0, 0, 4),
            Pos(-4, 0, 0),
            Pos(0, 0, -4)
        )

        private fun getInfusionPlatforms(world: World, x: Int, y: Int, z: Int): MutableList<PosPair> {
            val items = ArrayList<PosPair>()
            for (pos in platformPositions) {
                val tile = pos.getTileAt(world, x, y, z)
                if (tile is TileItemDisplay) {
                    val stack = tile.getStackInSlot(0)
                    if (stack != null) items.add(PosPair(pos, stack))
                }
            }
            return items
        }

        private fun replaceItemInHand(player: EntityPlayer, oldStack: ItemStack, newStack: ItemStack): Boolean {
            val stackInSlot = player.heldItem
            if (stackInSlot != null && stackInSlot.item == oldStack.item && (stackInSlot.itemDamage == oldStack.itemDamage || oldStack.itemDamage == OreDictionary.WILDCARD_VALUE || stackInSlot.item.isDamageable)) {
                newStack.stackSize = oldStack.stackSize
                newStack.stackTagCompound = stackInSlot.tagCompound
                player.setCurrentItemOrArmor(0, newStack)
                return true
            }
            return false
        }
    }

    @SubscribeEvent
    fun someoneSaidSomething(whatWasIt: ServerChatEvent) {
        for (handler in handlers)
            if (handler.execute(whatWasIt))
                return
    }
}
