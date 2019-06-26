package alfheim.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * ModelSimpleAnyavil - AlexSocol
 * Created using Tabula 4.1.1
 */
public class ModelSimpleAnyavil extends ModelBase {
	
    public final ModelRenderer shape1;
    public final ModelRenderer shape2;
    public final ModelRenderer shape3;
    public final ModelRenderer shape4;

    public ModelSimpleAnyavil() {
        textureWidth = 52;
        textureHeight = 50;
        shape1 = new ModelRenderer(this, 0, 16);
        shape1.setRotationPoint(-6.0F, 20.0F, -6.0F);
        shape1.addBox(0.0F, 0.0F, 0.0F, 12, 4, 12, 0.0F);
        shape2 = new ModelRenderer(this, 0, 32);
        shape2.setRotationPoint(-5.0F, 19.0F, -4.0F);
        shape2.addBox(0.0F, 0.0F, 0.0F, 10, 1, 8, 0.0F);
        shape3 = new ModelRenderer(this, 0, 41);
        shape3.setRotationPoint(-4.0F, 14.0F, -2.0F);
        shape3.addBox(0.0F, 0.0F, 0.0F, 8, 5, 4, 0.0F);
        shape4 = new ModelRenderer(this, 0, 0);
        shape4.setRotationPoint(-8.0F, 8.0F, -5.0F);
        shape4.addBox(0.0F, 0.0F, 0.0F, 16, 6, 10, 0.0F);
    }

    public void renderAll() {
    	shape1.render(0.0625F);
        shape2.render(0.0625F);
        shape3.render(0.0625F);
        shape4.render(0.0625F);
    }
}
