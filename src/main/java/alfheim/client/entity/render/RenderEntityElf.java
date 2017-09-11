package alfheim.client.entity.render;

import alfheim.Constants;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntityElf extends RenderLiving {

    public RenderEntityElf(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity par1Entity) {
        return new ResourceLocation(Constants.MODID, "textures/model/entity/Elf.png");
    }
}
