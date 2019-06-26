package alexsocol.asjlib.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

/**
 * Class representing 3-point vector
 * @author ChickenBones, Vazkii, AlexSocol
 */
public class Vector3 implements Serializable {
	
	private static final long serialVersionUID = 84148136481306L;
	
	public static final transient Vector3 fallback = new Vector3(-1, -1, -1);
	public static final transient Vector3 zero = new Vector3();
	public static final transient Vector3 one = new Vector3(1, 1, 1);
	public static final transient Vector3 center = new Vector3(0.5, 0.5, 0.5);
	public static final transient Vector3 oX = new Vector3(1, 0, 0);
	public static final transient Vector3 oY = new Vector3(0, 1, 0);
	public static final transient Vector3 oZ = new Vector3(0, 0, 1);
	
	public double x;
	public double y;
	public double z;
	
	public Vector3() {}
	
	public Vector3(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
	}
	
	public Vector3(Vector3 vec) {
		set(vec);
	}
	
	public Vector3(Vec3 vec) {
		x = vec.xCoord;
		y = vec.yCoord;
		z = vec.zCoord;
	}
	
	public Vec3 toVec3() {
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public Vector3 copy() {
		return new Vector3(this);
	}
	
	public static Vector3 fromEntity(Entity e) {
		return new Vector3(e.posX, e.posY, e.posZ);
	}
	
	public static Vector3 fromEntityCenter(Entity e) {
		return new Vector3(e.posX, e.posY - e.yOffset + e.height / 2, e.posZ);
	}
	
	public static Vector3 fromTileEntity(TileEntity e) {
		return new Vector3(e.xCoord, e.yCoord, e.zCoord);
	}
	
	public static Vector3 fromTileEntityCenter(TileEntity e) {
		return new Vector3(e.xCoord + 0.5, e.yCoord + 0.5, e.zCoord + 0.5);
	}
	
	public static double vecDistance(Vector3 v1, Vector3 v2) {
		return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2) + Math.pow(v1.z - v2.z, 2));
	}
	
	public static double vecEntityDistance(Vector3 v, Entity e) {
		return Math.sqrt(Math.pow(v.x - e.posX, 2) + Math.pow(v.y - e.posY, 2) + Math.pow(v.z - e.posZ, 2));
	}
	
	public static double vecTileDistance(Vector3 v, TileEntity te) {
		return Math.sqrt(Math.pow(v.x - te.xCoord, 2) + Math.pow(v.y - te.yCoord, 2) + Math.pow(v.z - te.zCoord, 2));
	}
	
	public static double entityTileDistance(Entity e, TileEntity te) {
		return Math.sqrt(Math.pow(e.posX - te.xCoord, 2) + Math.pow(e.posY - te.yCoord, 2) + Math.pow(e.posZ - te.zCoord, 2));
	}
	
	public static double entityDistance(Entity e1, Entity e2) {
		return Math.sqrt(Math.pow(e1.posX - e2.posX, 2) + Math.pow(e1.posY - e2.posY, 2) + Math.pow(e1.posZ - e2.posZ, 2));
	}
	
	public static double entityDistancePlane(Entity e1, Entity e2) {
		return  Math.hypot(e1.posX - e2.posX, e1.posZ - e2.posZ);
	}
	
	public Vector3 discard() {
		return set(0, 0, 0);
	}
	
	public Vector3 set(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
		return this;
	}
	
	public Vector3 set(Vec3 vec) {
		x = vec.xCoord;
		y = vec.yCoord;
		z = vec.zCoord;
		return this;
	}
	
	public Vector3 set(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vector3 set(Entity e) {
		x = e.posX;
		y = e.posY;
		z = e.posZ;
		return this;
	}
	
	public Vector3 set(TileEntity te) {
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		return this;
	}
	
	public Vector3 rand() {
		x = Math.random();
		y = Math.random();
		z = Math.random();
		return this;
	}
	
	public double dotProduct(Vector3 vec) {
		double d = vec.x * x + vec.y * y + vec.z * z;
		
		if(d > 1 && d < 1.00001)
			d = 1;
		else if(d < -1 && d > -1.00001)
			d = -1;
		return d;
	}
	
	public double dotProduct(double d, double d1, double d2) {
		return d * x + d1 * y + d2 * z;
	}
	
	public Vector3 crossProduct(Vector3 vec) {
		double d = y * vec.z - z * vec.y;
		double d1 = z * vec.x - x * vec.z;
		double d2 = x * vec.y - y * vec.x;
		x = d;
		y = d1;
		z = d2;
		return this;
	}
	
	public Vector3 add(double d) {
		return add(d, d, d);
	}
	
	public Vector3 add(double d, double d1, double d2) {
		x += d;
		y += d1;
		z += d2;
		return this;
	}
	
	public Vector3 add(Vector3 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}
	
	public Vector3 add(Entity e) {
		x += e.posX;
		y += e.posY;
		z += e.posZ;
		return this;
	}
	
	public Vector3 add(TileEntity te) {
		x += te.xCoord;
		y += te.yCoord;
		z += te.zCoord;
		return this;
	}
	
	public Vector3 sub(double d) {
		return sub(d, d, d);
	}
	
	public Vector3 sub(double d, double d1, double d2) {
		x -= d;
		y -= d1;
		z -= d2;
		return this;
	}
	
	public Vector3 sub(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}
	
	public Vector3 sub(Entity e) {
		x -= e.posX;
		y -= e.posY;
		z -= e.posZ;
		return this;
	}
	
	public Vector3 sub(TileEntity te) {
		x -= te.xCoord;
		y -= te.yCoord;
		z -= te.zCoord;
		return this;
	}
	
	public Vector3 negate(Vector3 vec) {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public Vector3 mul(double d) {
		x *= d;
		y *= d;
		z *= d;
		return this;
	}
	
	public Vector3 mul(Vector3 f) {
		x *= f.x;
		y *= f.y;
		z *= f.z;
		return this;
	}
	
	public Vector3 mul(double fx, double fy, double fz) {
		x *= fx;
		y *= fy;
		z *= fz;
		return this;
	}
	
	public Vector3 extend(double d) {
		return set(copy().normalize().mul(Math.max(length() + d, 0)));
	}
	
	public Vector3 shrink(double d) {
		return set(copy().normalize().mul(Math.min(length() - d, 0)));
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public Vector3 normalize() {
		double d = length();
		if(d != 0)
			mul(1 / d);
		
		return this;
	}
	
	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vector3(" + new BigDecimal(x, cont) + ", " +new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
	}
	
	public Vector3 perpendicular() {
		if(z == 0)
			return zCrossProduct();
		return xCrossProduct();
	}
	
	public Vector3 xCrossProduct() {
		double d = z;
		double d1 = -y;
		x = 0;
		y = d;
		z = d1;
		return this;
	}
	
	public Vector3 zCrossProduct() {
		double d = y;
		
		double d1 = -x;
		x = d;
		y = d1;
		z = 0;
		return this;
	}
	
	public Vector3 yCrossProduct() {
		double d = -z;
		double d1 = x;
		x = d;
		y = 0;
		z = d1;
		return this;
	}
	
	public Vec3 toVec3D() {
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public double angle(Vector3 vec) {
		return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
	}
	
	public boolean isInside(AxisAlignedBB aabb) {
		return x >= aabb.minX && y >= aabb.maxY && z >= aabb.minZ && x < aabb.maxX && y < aabb.maxY && z < aabb.maxZ;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	public boolean isAxial() {
		return x == 0 ? y == 0 || z == 0 : y == 0 && z == 0;
	}
	
	@SideOnly(Side.CLIENT)
	public Vector3f vector3f() {
		return new Vector3f((float)x, (float)y, (float)z);
	}
	
	@SideOnly(Side.CLIENT)
	public Vector4f vector4f() {
		return new Vector4f((float)x, (float)y, (float)z, 1);
	}
	
	@SideOnly(Side.CLIENT)
	public Vector3 glVertex() {
		org.lwjgl.opengl.GL11.glVertex3d(x, y, z);
		return this;
	}
	
	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public double scalarProject(Vector3 b) {
		double l = b.length();
		return l == 0 ? 0 : dotProduct(b)/l;
	}
	
	public Vector3 project(Vector3 b) {
		double l = b.lengthSquared();
		if(l == 0) {
			set(0, 0, 0);
			return this;
		}
		
		double m = dotProduct(b)/l;
		set(b).mul(m);
		return this;
	}
	
	public Vector3 rotate(double angle, Vector3 axis) {
		Quaternion.aroundAxis(axis.copy().normalize(), Math.toRadians(angle)).rotate(this);
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Vector3))
			return false;
		
		Vector3 v = (Vector3)o;
		return x == v.x && y == v.y && z == v.z;
	}
}