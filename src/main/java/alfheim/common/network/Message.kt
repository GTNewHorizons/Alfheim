package alfheim.common.network

//public class MessageDelegate<T>() : ReadWriteProperty<Any, T> {
//
//
//    @Suppress("UNCHECKED_CAST")
//    override fun getValue(thisRef: Any, property: KProperty<*>) : T {
//        var name = property.name
//        when (thisRef) {
//            is Byte -> {return buf.readByte()}
//            is Float -> {return prefs.getFloat(name, defaultValue as Float) as T}
//            is Int -> {return prefs.getInt(name, defaultValue as Int) as T}
//            is Long -> {return prefs.getLong(name, defaultValue as Long) as T}
//            is String -> {return prefs.getString(name, defaultValue as String) as T}
//            else -> throw UnsupportedOperationException("Unsupported preference type ${property.javaClass} on property $name")
//        }
//    }
//
//    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
//        var name = property.name
//        when (defaultValue) {
//            is Boolean -> {editor.putBoolean(name, value as Boolean)}
//            is Float -> {editor.putFloat(name, value as Float)}
//            is Int -> {editor.putInt(name, value as Int)}
//            is Long -> {editor.putLong(name, value as Long)}
//            is String -> {editor.putString(name, value as String)}
//            else -> throw UnsupportedOperationException("Unsupported preference type ${property.javaClass} on property $name")
//        }
//
//        editor.commit()
//    }
//}
//
//private static byte readByte(ByteBuf buf) {
//    return buf.readByte();
//}
//
//private static void writeByte(byte b, ByteBuf buf) {
//    buf.writeByte(b);
//}
//
//private static short readShort(ByteBuf buf) {
//    return buf.readShort();
//}
//
//private static void writeShort(short s, ByteBuf buf) {
//    buf.writeShort(s);
//}
//
//private static int readInt(ByteBuf buf) {
//    return buf.readInt();
//}
//
//private static void writeInt(int i, ByteBuf buf) {
//    buf.writeInt(i);
//}
//
//private static long readLong(ByteBuf buf) {
//    return buf.readLong();
//}
//
//private static void writeLong(long l, ByteBuf buf) {
//    buf.writeLong(l);
//}
//
//private static float readFloat(ByteBuf buf) {
//    return buf.readFloat();
//}
//
//private static void writeFloat(float f, ByteBuf buf) {
//    buf.writeFloat(f);
//}
//
//private static double readDouble(ByteBuf buf) {
//    return buf.readDouble();
//}
//
//private static void writeDouble(double d, ByteBuf buf) {
//    buf.writeDouble(d);
//}
//
//private static boolean readBoolean(ByteBuf buf) {
//    return buf.readBoolean();
//}
//
//private static void writeBoolean(boolean b, ByteBuf buf) {
//    buf.writeBoolean(b);
//}
//
//private static char readChar(ByteBuf buf) {
//    return buf.readChar();
//}
//
//private static void writeChar(char c, ByteBuf buf) {
//    buf.writeChar(c);
//}
//
//private static String readString(ByteBuf buf) {
//    return ByteBufUtils.readUTF8String(buf);
//}
//
//private static void writeString(String s, ByteBuf buf) {
//    ByteBufUtils.writeUTF8String(buf, s);
//}
//
//private static NBTTagCompound readNBT(ByteBuf buf) {
//    return ByteBufUtils.readTag(buf);
//}
//
//private static void writeNBT(NBTTagCompound cmp, ByteBuf buf) {
//    ByteBufUtils.writeTag(buf, cmp);
//}
//
//private static ItemStack readItemStack(ByteBuf buf) {
//    return ByteBufUtils.readItemStack(buf);
//}
//
//private static void writeItemStack(ItemStack stack, ByteBuf buf) {
//    ByteBufUtils.writeItemStack(buf, stack);
//}
//
//private static BlockPos readBlockPos(ByteBuf buf) {
//    return BlockPos.fromLong(buf.readLong());
//}
//
//private static void writeBlockPos(BlockPos pos, ByteBuf buf) {
//    buf.writeLong(pos.toLong());
//}
//
