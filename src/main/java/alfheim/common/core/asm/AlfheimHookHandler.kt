package alfheim.common.core.asm

import alfheim.api.event.NetherPortalActivationEvent
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.utils.AlfheimConfig
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.potion.PotionSoulburn
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.ReturnCondition
import net.minecraft.block.Block
import net.minecraft.block.BlockPortal
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.common.Botania
import vazkii.botania.common.block.BlockPylon
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TilePylon
import vazkii.botania.common.item.relic.ItemFlugelEye

class AlfheimHookHandler {
    
    companion object {
        private var updatingEntity = false
        private val TAG_TRANSFER_STACK = "transferStack"

        @Hook
        @JvmStatic
        fun updateEntity(entity: TilePylon) {
            if (entity.getWorldObj().isRemote) updatingEntity = true
        }

        @Hook(injectOnExit = true, targetMethod = "updateEntity")
        @JvmStatic
        fun updateEntityPost(entity: TilePylon) {
            if (entity.getWorldObj().isRemote) {
                updatingEntity = false
                if (entity.getWorldObj().rand.nextBoolean()) {
                    val meta = entity.getBlockMetadata()
                    Botania.proxy.sparkleFX(entity.getWorldObj(), entity.xCoord + Math.random(), entity.yCoord + Math.random() * 1.5, entity.zCoord + Math.random(), if (meta == 2) 0F else 0.5F, if (meta == 0) 0.5F else 1F, if (meta == 1) 0.5F else 1F, Math.random().toFloat(), 2)
                }
            }
        }

        @Hook(returnCondition = ReturnCondition.ON_TRUE)
        @JvmStatic
        fun sparkleFX(proxy: ClientProxy, world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, m: Int, fake: Boolean): Boolean = updatingEntity

        @Hook(returnCondition = ReturnCondition.ALWAYS)
        @JvmStatic
        fun getIcon(pylon: BlockPylon, side: Int, meta: Int): IIcon = if (meta == 0 || meta == 1) ModBlocks.storage.getIcon(side, meta) else Blocks.diamond_block.getIcon(side, 0)

        @Hook(returnCondition = ReturnCondition.ON_TRUE, targetMethod = "func_150000_e", isMandatory = true)
        @JvmStatic
        fun onNetherPortalActivation(portal: BlockPortal, world: World, x: Int, y: Int, z: Int): Boolean = MinecraftForge.EVENT_BUS.post(NetherPortalActivationEvent(world, x, y, z))

        @Hook(returnCondition = ReturnCondition.ON_TRUE, booleanReturnConstant = false, isMandatory = true)
        @JvmStatic
        fun matches(recipe: RecipePureDaisy, world: World, x: Int, y: Int, z: Int, pureDaisy: SubTileEntity, block: Block, meta: Int): Boolean = recipe.getOutput().equals(ModBlocks.livingwood) && world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim

        @Hook(returnCondition = ReturnCondition.ON_TRUE, isMandatory = true)
        @JvmStatic
        fun onItemUse(eye: ItemFlugelEye, stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = if (player.isSneaking() && world.getBlock(x, y, z) == Blocks.beacon) EntityFlugel.spawn(player, stack, world, x, y, z) else false

        @SideOnly(Side.CLIENT)
        @Hook(injectOnExit = true)
        @JvmStatic
        fun renderManaBar(hh: HUDHandler, x: Int, y: Int, color: Int, alpha: Float, mana: Int, maxMana: Int) {
            if (mana < 0) return
            GL11.glPushMatrix()
            var text = "$mana/$maxMana"
            var X = x + 51 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2
            var Y = y - 19

            Minecraft.getMinecraft().fontRenderer.drawString(text, X, Y, color, true)
            GL11.glPopMatrix()
        }

        @SideOnly(Side.CLIENT)
        @Hook
        @JvmStatic
        fun doRenderShadowAndFire(render: Render, entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTicks: Float) {
            if (entity is EntityLivingBase && entity.getActivePotionEffect(AlfheimRegistry.soulburn) != null) PotionSoulburn.renderEntityOnFire(render, entity, x, y, z, partialTicks)
        }

        @SideOnly(Side.CLIENT)
        @Hook
        @JvmStatic
        fun renderOverlays(renderer: ItemRenderer, partialTicks: Float) {
            if (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(AlfheimRegistry.soulburn) != null) {
                GL11.glDisable(GL11.GL_ALPHA_TEST)
                PotionSoulburn.renderFireInFirstPerson(partialTicks)
                GL11.glEnable(GL11.GL_ALPHA_TEST)
            }
        }
    }
}