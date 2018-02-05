package alfheim.client.core.utils;

import alexsocol.asjlib.ASJUtilities;
import alfheim.ModInfo;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.render.tile.RenderTilePylon;

public class AlfheimBotaniaModifiers {

	public static final ResourceLocation texturePinkOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylonOld.png");
	public static final ResourceLocation texturePink = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylon.png");
	
	public static void postInit() {
		ASJUtilities.setFinalField(new RenderTilePylon(), texturePink, "texturePink");
		ASJUtilities.setFinalField(new RenderTilePylon(), texturePinkOld, "texturePinkOld");
	}
}