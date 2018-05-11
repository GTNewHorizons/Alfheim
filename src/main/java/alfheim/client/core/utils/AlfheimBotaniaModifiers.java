package alfheim.client.core.utils;

import alexsocol.asjlib.ASJReflectionHelper;
import alfheim.api.ModInfo;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.render.tile.RenderTilePylon;

public class AlfheimBotaniaModifiers {

	public static final ResourceLocation texturePinkOld = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylonOld.png");
	public static final ResourceLocation texturePink = new ResourceLocation(ModInfo.MODID, "textures/model/block/GaiaPylon.png");
	
	public static void postInit() {
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon.class, texturePink, "texturePink");
		ASJReflectionHelper.setStaticFinalValue(RenderTilePylon.class, texturePinkOld, "texturePinkOld");
	}
}