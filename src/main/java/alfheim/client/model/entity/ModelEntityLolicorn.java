package alfheim.client.model.entity;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.EntityLolicorn;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.MathHelper;

public class ModelEntityLolicorn extends ModelBase {
	
	private ModelRenderer head;
	private ModelRenderer mouthTop;
	private ModelRenderer mouthBottom;
	private ModelRenderer horseLeftEar;
	private ModelRenderer horseRightEar;
	private ModelRenderer neck;
	/**
	 * The box for the horse's ropes on its face.
	 */
	private ModelRenderer horseFaceRopes;
	private ModelRenderer mane;
	private ModelRenderer body;
	private ModelRenderer tailBase;
	private ModelRenderer tailMiddle;
	private ModelRenderer tailTip;
	private ModelRenderer backLeftLeg;
	private ModelRenderer backLeftShin;
	private ModelRenderer backLeftHoof;
	private ModelRenderer backRightLeg;
	private ModelRenderer backRightShin;
	private ModelRenderer backRightHoof;
	private ModelRenderer frontLeftLeg;
	private ModelRenderer frontLeftShin;
	private ModelRenderer frontLeftHoof;
	private ModelRenderer frontRightLeg;
	private ModelRenderer frontRightShin;
	private ModelRenderer frontRightHoof;
	private ModelRenderer horseSaddleBottom;
	private ModelRenderer horseSaddleFront;
	private ModelRenderer horseSaddleBack;
	private ModelRenderer horseLeftSaddleRope;
	private ModelRenderer horseLeftSaddleMetal;
	private ModelRenderer horseRightSaddleRope;
	private ModelRenderer horseRightSaddleMetal;
	/**
	 * The left metal connected to the horse's face ropes.
	 */
	private ModelRenderer horseLeftFaceMetal;
	/**
	 * The right metal connected to the horse's face ropes.
	 */
	private ModelRenderer horseRightFaceMetal;
	private ModelRenderer horseLeftRein;
	private ModelRenderer horseRightRein;
	
	public ModelEntityLolicorn() {
		textureWidth = 128;
		textureHeight = 128;
		body = new ModelRenderer(this, 0, 34);
		body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
		body.setRotationPoint(0.0F, 11.0F, 9.0F);
		tailBase = new ModelRenderer(this, 44, 0);
		tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
		tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailBase, -1.134464F, 0.0F, 0.0F);
		tailMiddle = new ModelRenderer(this, 38, 7);
		tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
		tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailMiddle, -1.134464F, 0.0F, 0.0F);
		tailTip = new ModelRenderer(this, 24, 3);
		tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
		tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailTip, -1.40215F, 0.0F, 0.0F);
		backLeftLeg = new ModelRenderer(this, 78, 29);
		backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
		backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
		backLeftShin = new ModelRenderer(this, 78, 43);
		backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
		backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
		backLeftHoof = new ModelRenderer(this, 78, 51);
		backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
		backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
		backRightLeg = new ModelRenderer(this, 96, 29);
		backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
		backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
		backRightShin = new ModelRenderer(this, 96, 43);
		backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
		backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
		backRightHoof = new ModelRenderer(this, 96, 51);
		backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
		backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
		frontLeftLeg = new ModelRenderer(this, 44, 29);
		frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
		frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
		frontLeftShin = new ModelRenderer(this, 44, 41);
		frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
		frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
		frontLeftHoof = new ModelRenderer(this, 44, 51);
		frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
		frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
		frontRightLeg = new ModelRenderer(this, 60, 29);
		frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
		frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
		frontRightShin = new ModelRenderer(this, 60, 41);
		frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
		frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
		frontRightHoof = new ModelRenderer(this, 60, 51);
		frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
		frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
		head.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(head, 0.5235988F, 0.0F, 0.0F);
		mouthTop = new ModelRenderer(this, 24, 18);
		mouthTop.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
		mouthTop.setRotationPoint(0.0F, 3.95F, -10.0F);
		setBoxRotation(mouthTop, 0.5235988F, 0.0F, 0.0F);
		mouthBottom = new ModelRenderer(this, 24, 27);
		mouthBottom.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
		mouthBottom.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(mouthBottom, 0.5235988F, 0.0F, 0.0F);
		head.addChild(mouthTop);
		head.addChild(mouthBottom);
		horseLeftEar = new ModelRenderer(this, 0, 0);
		horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
		horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseLeftEar, 0.5235988F, 0.0F, 0.0F);
		horseRightEar = new ModelRenderer(this, 0, 0);
		horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
		horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseRightEar, 0.5235988F, 0.0F, 0.0F);
		neck = new ModelRenderer(this, 0, 12);
		neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
		neck.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(neck, 0.5235988F, 0.0F, 0.0F);
		horseSaddleBottom = new ModelRenderer(this, 80, 0);
		horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
		horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseSaddleFront = new ModelRenderer(this, 106, 9);
		horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
		horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseSaddleBack = new ModelRenderer(this, 80, 9);
		horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
		horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
		horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
		horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
		horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
		horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
		horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
		horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
		horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
		horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
		horseRightSaddleRope = new ModelRenderer(this, 80, 0);
		horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
		horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
		horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
		horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
		horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseLeftFaceMetal, 0.5235988F, 0.0F, 0.0F);
		horseRightFaceMetal = new ModelRenderer(this, 74, 13);
		horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
		horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseRightFaceMetal, 0.5235988F, 0.0F, 0.0F);
		horseLeftRein = new ModelRenderer(this, 44, 10);
		horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
		horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
		horseRightRein = new ModelRenderer(this, 44, 5);
		horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
		horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
		mane = new ModelRenderer(this, 58, 0);
		mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
		mane.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(mane, 0.5235988F, 0.0F, 0.0F);
		horseFaceRopes = new ModelRenderer(this, 80, 12);
		horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
		horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseFaceRopes, 0.5235988F, 0.0F, 0.0F);
	}
	
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		horseFaceRopes.render(f5);
		horseSaddleBottom.render(f5);
		horseSaddleFront.render(f5);
		horseSaddleBack.render(f5);
		horseLeftSaddleRope.render(f5);
		horseLeftSaddleMetal.render(f5);
		horseRightSaddleRope.render(f5);
		horseRightSaddleMetal.render(f5);
		horseLeftFaceMetal.render(f5);
		horseRightFaceMetal.render(f5);
		
		if (entity.riddenByEntity != null) {
			horseLeftRein.render(f5);
			horseRightRein.render(f5);
		}
		
		backLeftLeg.render(f5);
		backLeftShin.render(f5);
		backLeftHoof.render(f5);
		backRightLeg.render(f5);
		backRightShin.render(f5);
		backRightHoof.render(f5);
		frontLeftLeg.render(f5);
		frontLeftShin.render(f5);
		frontLeftHoof.render(f5);
		frontRightLeg.render(f5);
		frontRightShin.render(f5);
		frontRightHoof.render(f5);
		
		body.render(f5);
		tailBase.render(f5);
		tailMiddle.render(f5);
		tailTip.render(f5);
		neck.render(f5);
		mane.render(f5);
		
		horseLeftEar.render(f5);
		horseRightEar.render(f5);
		
		head.render(f5);
	}
	
	/**
	 * Sets the rotations for a ModelRenderer in the ModelHorse class.
	 */
	private void setBoxRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	private float updateHorseRotation(float prevYaw, float yaw, float ticks) {
		float f3;
		
		f3 = yaw - prevYaw;
		while (f3 < -180.0F) {
			f3 += 360.0F;
		}
		
		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}
		
		return prevYaw + f3 * ticks;
	}
	
	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	public void setLivingAnimations(EntityLivingBase entity, float f, float f1, float ticks) {
		super.setLivingAnimations(entity, f, f1, ticks);
		float f3 = updateHorseRotation(entity.prevRenderYawOffset, entity.renderYawOffset, ticks);
		float f4 = updateHorseRotation(entity.prevRotationYawHead, entity.rotationYawHead, ticks);
		float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ticks;
		float f6 = f4 - f3;
		float f7 = f5 / (180F / (float) Math.PI);
		
		if (f6 > 20.0F) f6 = 20.0F;
		
		if (f6 < -20.0F) f6 = -20.0F;
		
		if (f1 > 0.2F) f7 += MathHelper.cos(f * 0.4F) * 0.15F * f1;
		
		EntityLolicorn lolicorn = (EntityLolicorn) entity;
		float f8 = 0F;
		float f9 = (float) ASJUtilities.interpolate(lolicorn.getPrevRearingAmount(), lolicorn.getRearingAmount());
		float f10 = 1.0F - f9;
		float f11 = (float) ASJUtilities.interpolate(lolicorn.getPrevMouthOpenness(), lolicorn.getMouthOpenness());
		boolean flag = lolicorn.getTailMovement() != 0;
		boolean flag2 = lolicorn.riddenByEntity != null;
		float f12 = (float) entity.ticksExisted + ticks;
		float f13 = MathHelper.cos(f * 0.6662F + (float) Math.PI);
		float f14 = f13 * 0.8F * f1;
		head.rotationPointY = 4.0F;
		head.rotationPointZ = -10.0F;
		tailBase.rotationPointY = 3.0F;
		tailMiddle.rotationPointZ = 14.0F;
		body.rotateAngleX = 0.0F;
		head.rotateAngleX = 0.5235988F + f7;
		head.rotateAngleY = f6 / (180F / (float) Math.PI);
		head.rotateAngleX = f9 * (0.2617994F + f7) + f8 * 2.18166F + (1.0F - Math.max(f9, f8)) * head.rotateAngleX;
		head.rotateAngleY = f9 * (f6 / (180F / (float) Math.PI)) + (1.0F - Math.max(f9, f8)) * head.rotateAngleY;
		head.rotationPointY = f9 * -6.0F + f8 * 11.0F + (1.0F - Math.max(f9, f8)) * head.rotationPointY;
		head.rotationPointZ = f9 * -1.0F + f8 * -10.0F + (1.0F - Math.max(f9, f8)) * head.rotationPointZ;
		tailBase.rotationPointY = f9 * 9.0F + f10 * tailBase.rotationPointY;
		tailMiddle.rotationPointZ = f9 * 18.0F + f10 * tailMiddle.rotationPointZ;
		body.rotateAngleX = f9 * -((float) Math.PI / 4F) + f10 * body.rotateAngleX;
		horseLeftEar.rotationPointY = head.rotationPointY;
		horseRightEar.rotationPointY = head.rotationPointY;
		neck.rotationPointY = head.rotationPointY;
		mouthTop.rotationPointY = 0.02F;
		mouthBottom.rotationPointY = 0.0F;
		mane.rotationPointY = head.rotationPointY;
		horseLeftEar.rotationPointZ = head.rotationPointZ;
		horseRightEar.rotationPointZ = head.rotationPointZ;
		neck.rotationPointZ = head.rotationPointZ;
		mouthTop.rotationPointZ = 0.02F - f11 * 1.0F;
		mouthBottom.rotationPointZ = 0.0F + f11 * 1.0F;
		mane.rotationPointZ = head.rotationPointZ;
		horseLeftEar.rotateAngleX = head.rotateAngleX;
		horseRightEar.rotateAngleX = head.rotateAngleX;
		neck.rotateAngleX = head.rotateAngleX;
		mouthTop.rotateAngleX = 0.0F - 0.09424778F * f11;
		mouthBottom.rotateAngleX = 0.0F + 0.15707964F * f11;
		mane.rotateAngleX = head.rotateAngleX;
		horseLeftEar.rotateAngleY = head.rotateAngleY;
		horseRightEar.rotateAngleY = head.rotateAngleY;
		neck.rotateAngleY = head.rotateAngleY;
		mouthTop.rotateAngleY = 0.0F;
		mouthBottom.rotateAngleY = 0.0F;
		mane.rotateAngleY = head.rotateAngleY;
		float f18 = 0.2617994F * f9;
		float f19 = MathHelper.cos(f12 * 0.6F + (float) Math.PI);
		frontLeftLeg.rotationPointY = -2.0F * f9 + 9.0F * f10;
		frontLeftLeg.rotationPointZ = -2.0F * f9 + -8.0F * f10;
		frontRightLeg.rotationPointY = frontLeftLeg.rotationPointY;
		frontRightLeg.rotationPointZ = frontLeftLeg.rotationPointZ;
		backLeftShin.rotationPointY = backLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f18 + f10 * -f13 * 0.5F * f1) * 7.0F;
		backLeftShin.rotationPointZ = backLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f18 + f10 * -f13 * 0.5F * f1) * 7.0F;
		backRightShin.rotationPointY = backRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f18 + f10 * f13 * 0.5F * f1) * 7.0F;
		backRightShin.rotationPointZ = backRightLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f18 + f10 * f13 * 0.5F * f1) * 7.0F;
		float f20 = (-1.0471976F + f19) * f9 + f14 * f10;
		float f21 = (-1.0471976F + -f19) * f9 + -f14 * f10;
		frontLeftShin.rotationPointY = frontLeftLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f20) * 7.0F;
		frontLeftShin.rotationPointZ = frontLeftLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f20) * 7.0F;
		frontRightShin.rotationPointY = frontRightLeg.rotationPointY + MathHelper.sin(((float) Math.PI / 2F) + f21) * 7.0F;
		frontRightShin.rotationPointZ = frontRightLeg.rotationPointZ + MathHelper.cos(((float) Math.PI * 3F / 2F) + f21) * 7.0F;
		backLeftLeg.rotateAngleX = f18 + -f13 * 0.5F * f1 * f10;
		backLeftShin.rotateAngleX = -0.08726646F * f9 + (-f13 * 0.5F * f1 - Math.max(0.0F, f13 * 0.5F * f1)) * f10;
		backLeftHoof.rotateAngleX = backLeftShin.rotateAngleX;
		backRightLeg.rotateAngleX = f18 + f13 * 0.5F * f1 * f10;
		backRightShin.rotateAngleX = -0.08726646F * f9 + (f13 * 0.5F * f1 - Math.max(0.0F, -f13 * 0.5F * f1)) * f10;
		backRightHoof.rotateAngleX = backRightShin.rotateAngleX;
		frontLeftLeg.rotateAngleX = f20;
		frontLeftShin.rotateAngleX = (frontLeftLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F + f19 * 0.2F)) * f9 + (f14 + Math.max(0.0F, f13 * 0.5F * f1)) * f10;
		frontLeftHoof.rotateAngleX = frontLeftShin.rotateAngleX;
		frontRightLeg.rotateAngleX = f21;
		frontRightShin.rotateAngleX = (frontRightLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F - f19 * 0.2F)) * f9 + (-f14 + Math.max(0.0F, -f13 * 0.5F * f1)) * f10;
		frontRightHoof.rotateAngleX = frontRightShin.rotateAngleX;
		backLeftHoof.rotationPointY = backLeftShin.rotationPointY;
		backLeftHoof.rotationPointZ = backLeftShin.rotationPointZ;
		backRightHoof.rotationPointY = backRightShin.rotationPointY;
		backRightHoof.rotationPointZ = backRightShin.rotationPointZ;
		frontLeftHoof.rotationPointY = frontLeftShin.rotationPointY;
		frontLeftHoof.rotationPointZ = frontLeftShin.rotationPointZ;
		frontRightHoof.rotationPointY = frontRightShin.rotationPointY;
		frontRightHoof.rotationPointZ = frontRightShin.rotationPointZ;
		
		horseSaddleBottom.rotationPointY = f9 * 0.5F + f10 * 2.0F;
		horseSaddleBottom.rotationPointZ = f9 * 11.0F + f10 * 2.0F;
		horseSaddleFront.rotationPointY = horseSaddleBottom.rotationPointY;
		horseSaddleBack.rotationPointY = horseSaddleBottom.rotationPointY;
		horseLeftSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY;
		horseRightSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY;
		horseLeftSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY;
		horseRightSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY;
		horseSaddleFront.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseSaddleBack.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseLeftSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseRightSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseLeftSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseRightSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ;
		horseSaddleBottom.rotateAngleX = body.rotateAngleX;
		horseSaddleFront.rotateAngleX = body.rotateAngleX;
		horseSaddleBack.rotateAngleX = body.rotateAngleX;
		horseLeftRein.rotationPointY = head.rotationPointY;
		horseRightRein.rotationPointY = head.rotationPointY;
		horseFaceRopes.rotationPointY = head.rotationPointY;
		horseLeftFaceMetal.rotationPointY = head.rotationPointY;
		horseRightFaceMetal.rotationPointY = head.rotationPointY;
		horseLeftRein.rotationPointZ = head.rotationPointZ;
		horseRightRein.rotationPointZ = head.rotationPointZ;
		horseFaceRopes.rotationPointZ = head.rotationPointZ;
		horseLeftFaceMetal.rotationPointZ = head.rotationPointZ;
		horseRightFaceMetal.rotationPointZ = head.rotationPointZ;
		horseLeftRein.rotateAngleX = f7;
		horseRightRein.rotateAngleX = f7;
		horseFaceRopes.rotateAngleX = head.rotateAngleX;
		horseLeftFaceMetal.rotateAngleX = head.rotateAngleX;
		horseRightFaceMetal.rotateAngleX = head.rotateAngleX;
		horseFaceRopes.rotateAngleY = head.rotateAngleY;
		horseLeftFaceMetal.rotateAngleY = head.rotateAngleY;
		horseLeftRein.rotateAngleY = head.rotateAngleY;
		horseRightFaceMetal.rotateAngleY = head.rotateAngleY;
		horseRightRein.rotateAngleY = head.rotateAngleY;
		
		if (flag2) {
			horseLeftSaddleRope.rotateAngleX = -1.0471976F;
			horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
			horseRightSaddleRope.rotateAngleX = -1.0471976F;
			horseRightSaddleMetal.rotateAngleX = -1.0471976F;
			horseLeftSaddleRope.rotateAngleZ = 0.0F;
			horseLeftSaddleMetal.rotateAngleZ = 0.0F;
			horseRightSaddleRope.rotateAngleZ = 0.0F;
			horseRightSaddleMetal.rotateAngleZ = 0.0F;
		} else {
			horseLeftSaddleRope.rotateAngleX = f14 / 3.0F;
			horseLeftSaddleMetal.rotateAngleX = f14 / 3.0F;
			horseRightSaddleRope.rotateAngleX = f14 / 3.0F;
			horseRightSaddleMetal.rotateAngleX = f14 / 3.0F;
			horseLeftSaddleRope.rotateAngleZ = f14 / 5.0F;
			horseLeftSaddleMetal.rotateAngleZ = f14 / 5.0F;
			horseRightSaddleRope.rotateAngleZ = -f14 / 5.0F;
			horseRightSaddleMetal.rotateAngleZ = -f14 / 5.0F;
		}
		
		float f15 = -1.3089F + f1 * 1.5F;
		
		if (f15 > 0.0F) {
			f15 = 0.0F;
		}
		
		if (flag) {
			tailBase.rotateAngleY = MathHelper.cos(f12 * 0.7F);
			f15 = 0.0F;
		} else {
			tailBase.rotateAngleY = 0.0F;
		}
		
		tailMiddle.rotateAngleY = tailBase.rotateAngleY;
		tailTip.rotateAngleY = tailBase.rotateAngleY;
		tailMiddle.rotationPointY = tailBase.rotationPointY;
		tailTip.rotationPointY = tailBase.rotationPointY;
		tailMiddle.rotationPointZ = tailBase.rotationPointZ;
		tailTip.rotationPointZ = tailBase.rotationPointZ;
		tailBase.rotateAngleX = f15;
		tailMiddle.rotateAngleX = f15;
		tailTip.rotateAngleX = -0.2618F + f15;
	}
}