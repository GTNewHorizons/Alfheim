package alfheim.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * ModelButterfly - AlexSocol
 * Created using Tabula 4.1.1
 */
public class ModelButterfly extends ModelBase {
    
    public ModelRenderer LeftWing;
    public ModelRenderer RightWing;
    public ModelRenderer Antenas;
    public ModelRenderer Body;

    public ModelButterfly() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Body = new ModelRenderer(this, 0, 0);
        this.Body.setRotationPoint(-2.0F, 16.0F, -2.0F);
        this.Body.addBox(0.0F, 0.0F, 0.0F, 4, 3, 8, 0.0F);
        this.RightWing = new ModelRenderer(this, 32, -2);
        this.RightWing.setRotationPoint(-2.0F, 17.0F, 0.0F);
        this.RightWing.addBox(0.0F, -6.0F, -1.0F, 0, 18, 16, 0.0F);
        this.setRotateAngle(RightWing, 1.5707963267948966F, 0.0F, -1.5707963267948966F);
        this.Antenas = new ModelRenderer(this, 26, 0);
        this.Antenas.setRotationPoint(-2.0F, 16.0F, -2.0F);
        this.Antenas.addBox(-2.0F, -1.0F, 0.0F, 8, 8, 0, 0.0F);
        this.setRotateAngle(Antenas, -2.356194490192345F, 0.0F, 0.0F);
        this.LeftWing = new ModelRenderer(this, 0, -2);
        this.LeftWing.setRotationPoint(2.0F, 17.0F, 0.0F);
        this.LeftWing.addBox(0.0F, -6.0F, -1.0F, 0, 18, 16, 0.0F);
        this.setRotateAngle(LeftWing, 1.5707963267948966F, 0.0F, 1.5707963267948966F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body.render(f5);
        this.RightWing.render(f5);
        this.Antenas.render(f5);
        this.LeftWing.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        
        RightWing.rotateAngleZ = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F);
        LeftWing.rotateAngleZ = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F;
    }
}
