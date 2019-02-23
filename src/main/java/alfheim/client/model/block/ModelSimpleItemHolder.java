package alfheim.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelSimpleItemHolder - AlexSocol
 * Created using Tabula 4.1.1
 */
public class ModelSimpleItemHolder extends ModelBase {
	
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape6;
    public ModelRenderer shape7;
    public ModelRenderer shape8;
    public ModelRenderer shape9;
    public ModelRenderer shape10;
    public ModelRenderer shape11;
    public ModelRenderer shape12;
    public ModelRenderer shape13;
    public ModelRenderer shape14;
    public ModelRenderer shape15;
    public ModelRenderer shape16;
    public ModelRenderer shape17;
    public ModelRenderer shape18;
    public ModelRenderer shape19;
    public ModelRenderer shape20;
    public ModelRenderer shape21;

    public ModelSimpleItemHolder() {
        textureWidth = 16;
        textureHeight = 16;
        shape17 = new ModelRenderer(this, 0, 9);
        shape17.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape17.addBox(-1.0F, 0.0F, 4.0F, 2, 1, 1, 0.0F);
        setRotateAngle(shape17, 0.0F, 3.141592653589793F, 0.0F);
        shape3 = new ModelRenderer(this, -2, 13);
        shape3.setRotationPoint(-4.0F, 11.0F, -4.0F);
        shape3.addBox(0.0F, 0.0F, 0.0F, 8, 1, 2, 0.0F);
        shape9 = new ModelRenderer(this, 0, 9);
        shape9.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape9.addBox(-1.0F, 0.0F, 4.0F, 2, 1, 1, 0.0F);
        shape16 = new ModelRenderer(this, 0, 6);
        shape16.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape16.addBox(-1.0F, 0.0F, 5.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape16, 0.0F, 3.141592653589793F, 0.0F);
        shape5 = new ModelRenderer(this, 0, 0);
        shape5.setRotationPoint(-4.0F, 11.0F, -2.0F);
        shape5.addBox(0.0F, 0.0F, 0.0F, 2, 1, 4, 0.0F);
        shape4 = new ModelRenderer(this, 0, 8);
        shape4.setRotationPoint(2.0F, 11.0F, -2.0F);
        shape4.addBox(0.0F, 0.0F, 0.0F, 2, 1, 4, 0.0F);
        shape6 = new ModelRenderer(this, 0, 0);
        shape6.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape6.addBox(-1.0F, 2.0F, 7.0F, 2, 3, 1, 0.0F);
        shape7 = new ModelRenderer(this, 0, 3);
        shape7.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape7.addBox(-1.0F, 1.0F, 6.0F, 2, 2, 1, 0.0F);
        shape8 = new ModelRenderer(this, 0, 6);
        shape8.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape8.addBox(-1.0F, 0.0F, 5.0F, 2, 2, 1, 0.0F);
        shape12 = new ModelRenderer(this, 0, 6);
        shape12.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape12.addBox(-1.0F, 0.0F, 5.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape12, 0.0F, 1.5707963267948966F, 0.0F);
        shape14 = new ModelRenderer(this, 0, 0);
        shape14.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape14.addBox(-1.0F, 2.0F, 7.0F, 2, 3, 1, 0.0F);
        setRotateAngle(shape14, 0.0F, 3.141592653589793F, 0.0F);
        shape20 = new ModelRenderer(this, 0, 6);
        shape20.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape20.addBox(-1.0F, 0.0F, 5.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape20, 0.0F, -1.5707963267948966F, 0.0F);
        shape11 = new ModelRenderer(this, 0, 3);
        shape11.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape11.addBox(-1.0F, 1.0F, 6.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape11, 0.0F, 1.5707963267948966F, 0.0F);
        shape21 = new ModelRenderer(this, 0, 9);
        shape21.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape21.addBox(-1.0F, 0.0F, 4.0F, 2, 1, 1, 0.0F);
        setRotateAngle(shape21, 0.0F, -1.5707963267948966F, 0.0F);
        shape2 = new ModelRenderer(this, -2, 0);
        shape2.setRotationPoint(-4.0F, 11.0F, 2.0F);
        shape2.addBox(0.0F, 0.0F, 0.0F, 8, 1, 2, 0.0F);
        shape10 = new ModelRenderer(this, 0, 0);
        shape10.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape10.addBox(-1.0F, 2.0F, 7.0F, 2, 3, 1, 0.0F);
        setRotateAngle(shape10, 0.0F, 1.5707963267948966F, 0.0F);
        shape15 = new ModelRenderer(this, 0, 3);
        shape15.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape15.addBox(-1.0F, 1.0F, 6.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape15, 0.0F, 3.141592653589793F, 0.0F);
        shape13 = new ModelRenderer(this, 0, 9);
        shape13.mirror = true;
        shape13.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape13.addBox(-1.0F, 0.0F, 4.0F, 2, 1, 1, 0.0F);
        setRotateAngle(shape13, 0.0F, 1.5707963267948966F, 0.0F);
        shape18 = new ModelRenderer(this, 0, 0);
        shape18.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape18.addBox(-1.0F, 2.0F, 7.0F, 2, 3, 1, 0.0F);
        setRotateAngle(shape18, 0.0F, -1.5707963267948966F, 0.0F);
        shape19 = new ModelRenderer(this, 0, 3);
        shape19.setRotationPoint(0.0F, 11.0F, 0.0F);
        shape19.addBox(-1.0F, 1.0F, 6.0F, 2, 2, 1, 0.0F);
        setRotateAngle(shape19, 0.0F, -1.5707963267948966F, 0.0F);
    }

    public void renderAll() { 
        shape17.render(0.0625F);
        shape3.render(0.0625F);
        shape9.render(0.0625F);
        shape16.render(0.0625F);
        shape5.render(0.0625F);
        shape4.render(0.0625F);
        shape6.render(0.0625F);
        shape7.render(0.0625F);
        shape8.render(0.0625F);
        shape12.render(0.0625F);
        shape14.render(0.0625F);
        shape20.render(0.0625F);
        shape11.render(0.0625F);
        shape21.render(0.0625F);
        shape2.render(0.0625F);
        shape10.render(0.0625F);
        shape15.render(0.0625F);
        shape13.render(0.0625F);
        shape18.render(0.0625F);
        shape19.render(0.0625F);
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
