package alfheim.client.gui;

import org.lwjgl.opengl.GL11;

import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.item.ModItems;

public class GUIManaTester extends Gui {
	
	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_MANA_TO_GET = "manaToGet";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private Minecraft mc;
	
	public GUIManaTester(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onDrawScreenPost(RenderGameOverlayEvent.Post e) {
		if(e.type != ElementType.ALL || !AlfheimConfig.numericalMana) return;
		ScaledResolution res = e.resolution;
		ItemStack equippedStack = mc.thePlayer.getCurrentEquippedItem();
		if(equippedStack != null && equippedStack.getItem() == ModItems.twigWand) {
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null) {
				TileEntity tile = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if(tile != null) {
					NBTTagCompound nbt = new NBTTagCompound();
					tile.writeToNBT(nbt);
					if (tile instanceof TileMod) ((TileMod) tile).writeCustomNBT(nbt);
					if (tile instanceof ISubTileContainer) {
						((ISubTileContainer)tile).getSubTile().writeToPacketNBT(nbt);
					}
					if (nbt.hasKey(TAG_MANA)) {
						String mana = Integer.valueOf(nbt.getInteger(TAG_MANA)).toString();
						if (nbt.hasKey(TAG_MANA_CAP)) {
							mana += ("/" + Integer.valueOf(nbt.getInteger(TAG_MANA_CAP)).toString());
						} else if (nbt.hasKey(TAG_MANA_TO_GET)) {
							mana += ("/" + Integer.valueOf(nbt.getInteger(TAG_MANA_TO_GET)).toString());
						} else if (nbt.hasKey(TAG_MANA_REQUIRED)) {
							mana += ("/" + Integer.valueOf(nbt.getInteger(TAG_MANA_REQUIRED)).toString());
						} else if (tile instanceof TileTerraPlate) {
							mana += ("/" + Integer.valueOf(((TileTerraPlate)tile).MAX_MANA).toString());
						} else if (tile instanceof TileManaInfuser) {
							mana += ("/" + Integer.valueOf(((TileManaInfuser)tile).MAX_MANA).toString());
						} else if (tile instanceof TileSpreader) {
							mana += ("/" + Integer.valueOf(((TileSpreader)tile).getMaxMana()).toString());
						} else if (tile instanceof TileBrewery) {
							mana += ("/" + Integer.valueOf(((TileBrewery)tile).getManaCost()).toString());
						} else if (tile instanceof ISubTileContainer) {
							SubTileEntity sub = ((ISubTileContainer)tile).getSubTile();
							if (sub instanceof SubTileGenerating) {
								mana += ("/" + Integer.valueOf(((SubTileGenerating)sub).getMaxMana()).toString());
							} else if (sub instanceof SubTileFunctional) {
								mana += ("/" + Integer.valueOf(((SubTileFunctional)sub).getMaxMana()).toString());
							}
						}
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(mana) / 2;
						int y = res.getScaledHeight() / 2 + 1;
						mc.fontRenderer.drawString(mana, x, y, 0x4444FF, true);
						GL11.glColor4f(1, 1, 1, 1);
						GL11.glDisable(GL11.GL_BLEND);
					}
				}
			}
		}
	}
}