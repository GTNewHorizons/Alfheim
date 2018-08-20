package alexsocol.asjlib.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASJPacketCompleter implements IClassTransformer {

	public static final List descriptors = Lists.newArrayList("Z", "B", "C", "D", "F", "I", "J", "S", "Ljava/lang/String;", "Lnet/minecraft/item/ItemStack;", "Lnet/minecraft/nbt/NBTTagCompound;");
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		ClassNode cl = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cl, 0);
		
		if (cl.superName != null && cl.superName.equals("alexsocol/asjlib/network/ASJPacket")) {
			boolean[] fs = new boolean[5]; // <init>, fromBytes, toBytes, fromCustomBytes, toCustomBytes
			for (MethodNode mt : cl.methods) {
				if (mt.name.equals("<init>") && mt.desc.equals("()V")) { fs[0] = true; continue; }
				if (mt.name.equals("fromBytes") && mt.desc.equals("(Lio/netty/buffer/ByteBuf;)V")) { fs[1] = true; continue; }
				if (mt.name.equals("toBytes") && mt.desc.equals("(Lio/netty/buffer/ByteBuf;)V")) { fs[2] = true; continue; }
				if (mt.name.equals("fromCustomBytes") && mt.desc.equals("(Lio/netty/buffer/ByteBuf;)V")) { fs[3] = true; continue; }
				if (mt.name.equals("toCustomBytes") && mt.desc.equals("(Lio/netty/buffer/ByteBuf;)V")) { fs[4] = true; continue; }
			}
			
			if (!fs[0]) makeConstructor(cl); 
			if (!fs[1]) makeFromBytes(cl, fs[3]); 
			if (!fs[2]) makeToBytes(cl, fs[4]); 
			
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			cl.accept(cw);
			
			return cw.toByteArray();
		}
		return basicClass;
	}
	
	public void makeConstructor(ClassNode cl) {
		MethodVisitor mv = cl.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, cl.superName, "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitEnd();
	}

	public void makeFromBytes(ClassNode cl, boolean callCustom) {
		MethodVisitor mv = cl.visitMethod(ACC_PUBLIC, "fromBytes", "(Lio/netty/buffer/ByteBuf;)V", null, null);
		mv.visitCode();
		if (callCustom) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, cl.name, "fromCustomBytes", "(Lio/netty/buffer/ByteBuf;)V", false);
		}
		for (FieldNode fn : getFileds(cl)) {
			if (!descriptors.contains(fn.desc)) continue;
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "alexsocol/asjlib/network/ASJPacket", "read" + fn.desc.replaceAll("/", "").replaceAll(";", ""), "(Lio/netty/buffer/ByteBuf;)" + fn.desc.replaceAll("/", "").replaceAll(";", ""), false);
			mv.visitFieldInsn(PUTFIELD, cl.name, fn.name, fn.desc);
		}
		mv.visitInsn(RETURN);
		mv.visitEnd();
	}
	
	public void makeToBytes(ClassNode cl, boolean callCustom) {
		MethodVisitor mv = cl.visitMethod(ACC_PUBLIC, "toBytes", "(Lio/netty/buffer/ByteBuf;)V", null, null);
		mv.visitCode();
		if (callCustom) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, cl.name, "toCustomBytes", "(Lio/netty/buffer/ByteBuf;)V", false);
		}
		for (FieldNode fn : getFileds(cl)) {
			if (!descriptors.contains(fn.desc)) continue;
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cl.name, fn.name, fn.desc);
			mv.visitMethodInsn(INVOKESTATIC, "alexsocol/asjlib/network/ASJPacket", "write", "(Lio/netty/buffer/ByteBuf;" + fn.desc + ")V", false);
		}
		mv.visitInsn(RETURN);
		mv.visitEnd();
	}
	
	public List<FieldNode> getFileds(ClassNode cl) {
		List<FieldNode> fns = new ArrayList<FieldNode>();
		for (FieldNode fn : cl.fields) {
			if ((fn.access & ACC_STATIC) > 0 || (fn.access & ACC_FINAL) > 0) continue;
			fns.add(fn);
		}
		return fns;
	}
}