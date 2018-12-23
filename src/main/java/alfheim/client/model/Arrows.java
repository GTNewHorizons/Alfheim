package alfheim.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Halo - Undefined
 * Created using Tabula 4.1.1
 */
public class Arrows extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;

    public Arrows() {
        this.textureWidth = 38;
        this.textureHeight = 20;
        this.shape1 = new ModelRenderer(this, 4, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 1, 16, 1, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 2);
        this.shape3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 16, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape1.render(f5);
        this.shape2.render(f5);
        this.shape3.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
