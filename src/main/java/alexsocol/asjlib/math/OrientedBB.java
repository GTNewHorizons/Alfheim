package alexsocol.asjlib.math;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

/**
 * Oriented Bound Box: scalable, translatable, rotatable!<br>
 * Collision calculations by <a href="https://github.com/gszauer/GamePhysicsCookbook">@gszauer</a><br>
 * <br>
 * Useful scheme:
<pre>
  h----g
 /|   /|
e----f |
| d--|-c
|/   |/	
a----b	
		
Y		
|  Z	
| /		
|/_____X
</pre>

 * @author AlexSocol
 */
public class OrientedBB {
	
	public Vector3 pos;			// center
	public Vector3 size;		// half size
	public Matrix4 orient;		// rotation (orientation) matrix
	
	public Vector3 a;
	public Vector3 b;
	public Vector3 c;
	public Vector3 d;
	public Vector3 e;
	public Vector3 f;
	public Vector3 g;
	public Vector3 h;
	
	public OrientedBB() {
		pos = new Vector3(0.5, 0.5, 0.5);
		size = new Vector3(0.5, 0.5, 0.5);
		orient = new Matrix4();
		
		a = new Vector3(0, 0, 0);
		b = new Vector3(1, 0, 0);
		c = new Vector3(1, 0, 1);
		d = new Vector3(0, 0, 1);
		e = new Vector3(0, 1, 0);
		f = new Vector3(1, 1, 0);
		g = new Vector3(1, 1, 1);
		h = new Vector3(0, 1, 1);
	}
	
	public OrientedBB(AxisAlignedBB aabb) {
		this();
		fromAABB(aabb);
	}
	
	/** Returns array of vertices for this BB */
	public Vector3[] vertices() {
		return new Vector3[] { a, b, c, d, e, f, g, h };
	}
	
	public OrientedBB fromAABB(AxisAlignedBB aabb) {
		pos.set(getAABBPosition(aabb));
		size.set(getAABBSize(aabb));
		
		a.set(aabb.minX, aabb.minY, aabb.minZ);
		b.set(aabb.maxX, aabb.minY, aabb.minZ);
		c.set(aabb.maxX, aabb.minY, aabb.maxZ);
		d.set(aabb.minX, aabb.minY, aabb.maxZ);
		e.set(aabb.minX, aabb.maxY, aabb.minZ);
		f.set(aabb.maxX, aabb.maxY, aabb.minZ);
		g.set(aabb.maxX, aabb.maxY, aabb.maxZ);
		h.set(aabb.minX, aabb.maxY, aabb.maxZ);
		
		return this;
	}
	
	/** Converts this OBB to AABB using min and max positions */
	public AxisAlignedBB toAABB() {
		ArrayList<Double> xs = Lists.newArrayList(a.x, b.x, c.x, d.x, e.x, f.x, g.x, h.x);
		ArrayList<Double> ys = Lists.newArrayList(a.y, b.y, c.y, d.y, e.y, f.y, g.y, h.y);
		ArrayList<Double> zs = Lists.newArrayList(a.z, b.z, c.z, d.z, e.z, f.z, g.z, h.z);
		return AxisAlignedBB.getBoundingBox(Collections.min(xs), Collections.min(ys), Collections.min(zs), Collections.max(xs), Collections.max(ys), Collections.max(zs));
	}
	
	/** Sets BB's center to this coords */
	public OrientedBB setPosition(double x, double y, double z) {
		pos.set(x, y, z);
		
		a.set(pos).add(-size.x, -size.y, -size.z);
		b.set(pos).add(size.x, -size.y, -size.z);
		c.set(pos).add(size.x, -size.y, size.z);
		d.set(pos).add(-size.x, -size.y, size.z);
		e.set(pos).add(-size.x, size.y, -size.z);
		f.set(pos).add(size.x, size.y, -size.z);
		g.set(pos).add(size.x, size.y, size.z);
		h.set(pos).add(-size.x, size.y, size.z);
		
		return this;
	}
	
	/** Moves BB on given distance */
	public OrientedBB translate(double x, double y, double z) {
		pos.add(x, y, z);
		
		a.add(x, y, z);
		b.add(x, y, z);
		c.add(x, y, z);
		d.add(x, y, z);
		e.add(x, y, z);
		f.add(x, y, z);
		g.add(x, y, z);
		h.add(x, y, z);
		
		return this;
	}
	
	/** Rescales BB in both directions from middle */
	public OrientedBB scale(double x, double y, double z) {
		size.mul(x, y, z);
		
		double i = pos.x, j = pos.y, k = pos.z;
		translate(-i, -j, -k);
		a.mul(x, y, z);
		b.mul(x, y, z);
		c.mul(x, y, z);
		d.mul(x, y, z);
		e.mul(x, y, z);
		f.mul(x, y, z);
		g.mul(x, y, z);
		h.mul(x, y, z);
		translate(i, j, k);
		
		return this;
	}
	
	/** Rotates BB on given angle in DEG around given axis (axis coords are global) */
	public OrientedBB rotate(double angle, Vector3 axis) {
		orient.rotate(Math.toRadians(angle), axis);
		pos.rotate(angle, axis);
		
		a.rotate(angle, axis);
		b.rotate(angle, axis);
		c.rotate(angle, axis);
		d.rotate(angle, axis);
		e.rotate(angle, axis);
		f.rotate(angle, axis);
		g.rotate(angle, axis);
		h.rotate(angle, axis);
		
		return this;
	}
	
	/** Rotates BB on given angle in DEG around given axis (axis coords are local, starting at pos[0, 0, 0]) */
	public OrientedBB rotateLocal(double angle, Vector3 axis) {
		double x = pos.x, y = pos.y, z = pos.z;
		translate(-x, -y, -z);
		rotate(angle, axis);
		translate(x, y, z);
		return this;
	}
	
	/** Rotates BB on given angle in DEG around middle point of ADHE (BCGF) face */
	public OrientedBB rotateOX(double angle) {
		double x = pos.x, y = pos.y, z = pos.z;
		translate(-x, -y, -z);
		rotate(angle, Vector3.oX);
		translate(x, y, z);
		return this;
	}
	
	/** Rotates BB on given angle in DEG around middle point of ABCD (EFGH) face */
	public OrientedBB rotateOY(double angle) {
		double x = pos.x, y = pos.y, z = pos.z;
		translate(-x, -y, -z);
		rotate(angle, Vector3.oY);
		translate(x, y, z);
		return this;
	}
	
	/** Rotates BB on given angle in DEG around middle point of ABFE (DCGH) face */
	public OrientedBB rotateOZ(double angle) {
		double x = pos.x, y = pos.y, z = pos.z;
		translate(-x, -y, -z);
		rotate(angle, Vector3.oZ);
		translate(x, y, z);
		return this;
	}
	
	/** Checks if this OBB intersects with given Vec3 */
	public boolean intersectsWith(Vec3 vec3) {
		return intersectsWith(this, new Vector3(vec3));
	}
	
	/** Checks if this OBB intersects with given Vec3 */
	public boolean intersectsWith(Vector3 vec3) {
		return intersectsWith(this, vec3);
	}
	
	/** Checks if this OBB intersects with given AABB */
	public boolean intersectsWith(AxisAlignedBB aabb) {
		return intersectsWith(this, aabb);
	}
	
	/** Checks if this OBB intersects with given OBB */
	public boolean intersectsWith(OrientedBB obb) {
		return intersectsWith(this, obb);
	}
	
	public static boolean intersectsWith(OrientedBB obb, Vector3 point) {
		Vector3 dir = point.copy().sub(obb.pos);
		double[] o = { obb.orient.m00, obb.orient.m01, obb.orient.m02, obb.orient.m10, obb.orient.m11, obb.orient.m12, obb.orient.m20, obb.orient.m21, obb.orient.m22 };
		double[] s = { obb.size.x, obb.size.y, obb.size.z };
		
		for (int i = 0; i < 3; ++i) {
			Vector3 axis = new Vector3(o[i * 3], o[i * 3 + 1], o[i * 3 + 2]);
			
			double distance = dir.dotProduct(axis);
			
			if (distance > s[i]) return false;
			if (distance < -s[i]) return false;
		}
		
		return true;
	}
	
	/** Checks if given OBB intersects with given AABB */
	public static boolean intersectsWith(OrientedBB obb, AxisAlignedBB aabb) {
		Vector3[] test = new Vector3[15];
		
		test[0] = new Vector3(1, 0, 0);			// AABB axis 1
		test[1] = new Vector3(0, 1, 0);			// AABB axis 2
		test[2] = new Vector3(0, 0, 1);			// AABB axis 3
		test[3] = new Vector3(obb.orient.m00, obb.orient.m01, obb.orient.m02);
		test[4] = new Vector3(obb.orient.m10, obb.orient.m11, obb.orient.m12);
		test[5] = new Vector3(obb.orient.m20, obb.orient.m21, obb.orient.m22);
		
		for (int i = 0; i < 3; ++i) { // Fill out rest of axis
			test[6 + i * 3] = test[i].copy().crossProduct(test[0]);
			test[6 + i * 3 + 1] = test[i].copy().crossProduct(test[1]);
			test[6 + i * 3 + 2] = test[i].copy().crossProduct(test[2]);
		}
		
		for (int i = 0; i < 15; ++i) {
			if (!overlapOnAxis(aabb, obb, test[i])) {
				return false; // Seperating axis found
			}
		}
		
		return true; // Seperating axis not found
	}
	
	/** Checks if one OBB intersects with other */
	public static boolean intersectsWith(OrientedBB obb1, OrientedBB obb2) {
		Vector3[] test = new Vector3[15];
		
		test[0] = new Vector3(obb1.orient.m00, obb1.orient.m01, obb1.orient.m02);
		test[1] = new Vector3(obb1.orient.m10, obb1.orient.m11, obb1.orient.m12);
		test[2] = new Vector3(obb1.orient.m20, obb1.orient.m21, obb1.orient.m22);
		test[3] = new Vector3(obb2.orient.m00, obb2.orient.m01, obb2.orient.m02);
		test[4] = new Vector3(obb2.orient.m10, obb2.orient.m11, obb2.orient.m12);
		test[5] = new Vector3(obb2.orient.m20, obb2.orient.m21, obb2.orient.m22);
		
		for (int i = 0; i < 3; ++i) { // Fill out rest of axis
			test[6 + i * 3] = test[i].copy().crossProduct(test[0]);
			test[6 + i * 3 + 1] = test[i].copy().crossProduct(test[1]);
			test[6 + i * 3 + 2] = test[i].copy().crossProduct(test[2]);
		}
		
		for (int i = 0; i < 15; ++i) {
			if (!overlapOnAxis(obb1, obb2, test[i])) {
				return false; // Seperating axis found
			}
		}
		
		return true; // Seperating axis not found
	}
	
	protected static boolean overlapOnAxis(AxisAlignedBB aabb, OrientedBB obb, Vector3 axis) {
		Interval a = getInterval(aabb, axis);
		Interval b = getInterval(obb, axis);
		return ((b.min <= a.max) && (a.min <= b.max));
	}
	
	protected static boolean overlapOnAxis(OrientedBB obb1, OrientedBB obb2, Vector3 axis) {
		Interval a = getInterval(obb1, axis);
		Interval b = getInterval(obb2, axis);
		return ((b.min <= a.max) && (a.min <= b.max));
	}
	
	protected static Interval getInterval(OrientedBB obb, Vector3 axis) {
		Vector3[] vertex = new Vector3[8];
		
		Vector3 c = new Vector3(obb.pos.x, obb.pos.y, obb.pos.z);		// OBB Center
		Vector3 e = new Vector3(obb.size.x, obb.size.y, obb.size.z);	// OBB Extents
		
		double[] o = { obb.orient.m00, obb.orient.m01, obb.orient.m02, obb.orient.m10, obb.orient.m11, obb.orient.m12, obb.orient.m20, obb.orient.m21, obb.orient.m22 };
		
		Vector3[] a = {					// OBB Axis
			new Vector3(o[0], o[1], o[2]),
			new Vector3(o[3], o[4], o[5]),
			new Vector3(o[6], o[7], o[8]),
		};
		
		vertex[0] = c.copy().add(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z));
		vertex[1] = c.copy().sub(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z));
		vertex[2] = c.copy().add(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z));
		vertex[3] = c.copy().add(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z));
		vertex[4] = c.copy().sub(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z));
		vertex[5] = c.copy().add(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z));
		vertex[6] = c.copy().sub(a[0].copy().mul(e.x)).add(a[1].copy().mul(e.y)).sub(a[2].copy().mul(e.z));
		vertex[7] = c.copy().sub(a[0].copy().mul(e.x)).sub(a[1].copy().mul(e.y)).add(a[2].copy().mul(e.z));
		
		Interval result = new Interval();
		result.min = result.max = axis.copy().dotProduct(vertex[0]);
		
		for (int i = 1; i < 8; ++i) {
			double projection = axis.copy().dotProduct(vertex[i]);
			result.min = (projection < result.min) ? projection : result.min;
			result.max = (projection > result.max) ? projection : result.max;
		}
		
		return result;
	}
	
	protected static Interval getInterval(AxisAlignedBB aabb, Vector3 axis) {
		Vector3 min = getMin(aabb);
		Vector3 max = getMax(aabb);
		
		Vector3[] vertex = {
				new Vector3(min.x, max.y, max.z),
				new Vector3(min.x, max.y, min.z),
				new Vector3(min.x, min.y, max.z),
				new Vector3(min.x, min.y, min.z),
				new Vector3(max.x, max.y, max.z),
				new Vector3(max.x, max.y, min.z),
				new Vector3(max.x, min.y, max.z),
				new Vector3(max.x, min.y, min.z)
		};
		
		Interval result = new Interval();
		result.min = result.max = axis.copy().dotProduct(vertex[0]);
		
		for (int i = 1; i < 8; ++i) {
			double projection = axis.copy().dotProduct(vertex[i]);
			result.min = (projection < result.min) ? projection : result.min;
			result.max = (projection > result.max) ? projection : result.max;
		}
		
		return result;
	}
	
	protected static Vector3 getMin(AxisAlignedBB aabb) {
		Vector3 p1 = getAABBPosition(aabb).add(getAABBSize(aabb));
		Vector3 p2 = getAABBPosition(aabb).sub(getAABBSize(aabb));
		
		return new Vector3(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
	}
	
	protected static Vector3 getMax(AxisAlignedBB aabb) {
		Vector3 p1 = getAABBPosition(aabb).add(getAABBSize(aabb));
		Vector3 p2 = getAABBPosition(aabb).sub(getAABBSize(aabb));
		
		return new Vector3(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
	}
	
	public static Vector3 getAABBPosition(AxisAlignedBB aabb) {
		return new Vector3((aabb.minX + aabb.maxX) / 2.0, (aabb.minY + aabb.maxY) / 2.0, (aabb.minZ + aabb.maxZ) / 2.0);
	}
	
	public static Vector3 getAABBSize(AxisAlignedBB aabb) {
		return new Vector3(Math.sqrt(Math.pow(aabb.minX - aabb.maxX, 2)) / 2.0, Math.sqrt(Math.pow(aabb.minY - aabb.maxY, 2)) / 2.0, Math.sqrt(Math.pow(aabb.minZ - aabb.maxZ, 2)) / 2.0);
	}
	
	protected static class Interval {
		public double min, max;
	}
	
	/** 
	 * Draws OBB on its position
	 * @param dt 0 to disable depth test
	 */
	@SideOnly(Side.CLIENT)
	public void draw(int dt) {
		glPushMatrix();
		if (dt == 0) glDisable(GL_DEPTH_TEST);
		glColor4d(0, 0, 0, 1);
		glPointSize(6);
		drawVertices();
		
		glColor4d(1, 0, 0, 1);
		glLineWidth(3);
		drawEdges();
		
		glColor4d(0, 0, 1, 0.5);
		drawFaces();
		if (dt == 0) glEnable(GL_DEPTH_TEST);
		glPopMatrix();
		
		glColor4d(1, 1, 1, 1);
	}
	
	@SideOnly(Side.CLIENT)
	public void drawVertices() {
		glBegin(GL_POINTS);
		for (Vector3 v : vertices()) {
			v.glVertex();
		}
		glEnd();
	}
	
	@SideOnly(Side.CLIENT)
	public void drawEdges() {
		glBegin(GL_LINES);
		a.glVertex();
		b.glVertex();
		b.glVertex();
		c.glVertex();
		c.glVertex();
		d.glVertex();
		d.glVertex();
		a.glVertex();
		
		e.glVertex();
		f.glVertex();
		f.glVertex();
		g.glVertex();
		g.glVertex();
		h.glVertex();
		h.glVertex();
		e.glVertex();
		
		a.glVertex();
		e.glVertex();
		b.glVertex();
		f.glVertex();
		c.glVertex();
		g.glVertex();
		d.glVertex();
		h.glVertex();
		glEnd();
	}
	
	@SideOnly(Side.CLIENT)
	public void drawFaces() {
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		a.glVertex();
		b.glVertex();
		c.glVertex();
		d.glVertex();
		
		h.glVertex();
		g.glVertex();
		f.glVertex();
		e.glVertex();
		
		e.glVertex();
		f.glVertex();
		b.glVertex();
		a.glVertex();
		
		f.glVertex();
		g.glVertex();
		c.glVertex();
		b.glVertex();
		
		g.glVertex();
		h.glVertex();
		d.glVertex();
		c.glVertex();
		
		h.glVertex();
		e.glVertex();
		a.glVertex();
		d.glVertex();
		
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
	}
}