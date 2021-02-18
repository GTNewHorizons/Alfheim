package alexsocol.asjlib

import net.minecraft.item.ItemStack
import net.minecraft.nbt.*

/** @author Vazkii
 */
object ItemNBTHelper {
	
	/** Checks if an ItemStack has a Tag Compound  */
	fun detectNBT(stack: ItemStack): Boolean {
		return stack.hasTagCompound()
	}
	
	/** Tries to initialize an NBT Tag Compound in an ItemStack,
	 * this will not do anything if the stack already has a tag
	 * compound  */
	fun initNBT(stack: ItemStack) {
		if (!detectNBT(stack)) injectNBT(stack, NBTTagCompound())
	}
	
	/** Injects an NBT Tag Compound to an ItemStack, no checks
	 * are made previously  */
	fun injectNBT(stack: ItemStack, nbt: NBTTagCompound?) {
		stack.tagCompound = nbt
	}
	
	/** Gets the NBTTagCompound in an ItemStack. Tries to init it
	 * previously in case there isn't one present  */
	fun getNBT(stack: ItemStack): NBTTagCompound {
		initNBT(stack)
		return stack.tagCompound
	}
	
	// SETTERS ///////////////////////////////////////////////////////////////////
	fun setBoolean(stack: ItemStack, tag: String, b: Boolean) {
		getNBT(stack).setBoolean(tag, b)
	}
	
	fun setByte(stack: ItemStack, tag: String, b: Byte) {
		getNBT(stack).setByte(tag, b)
	}
	
	fun setByteArray(stack: ItemStack, tag: String, array: ByteArray) {
		getNBT(stack).setByteArray(tag, array)
	}
	
	fun setShort(stack: ItemStack, tag: String, s: Short) {
		getNBT(stack).setShort(tag, s)
	}
	
	fun setInt(stack: ItemStack, tag: String, i: Int) {
		getNBT(stack).setInteger(tag, i)
	}
	
	fun setIntArray(stack: ItemStack, tag: String, array: IntArray) {
		getNBT(stack).setIntArray(tag, array)
	}
	
	fun setLong(stack: ItemStack, tag: String, l: Long) {
		getNBT(stack).setLong(tag, l)
	}
	
	fun setFloat(stack: ItemStack, tag: String, f: Float) {
		getNBT(stack).setFloat(tag, f)
	}
	
	fun setDouble(stack: ItemStack, tag: String, d: Double) {
		getNBT(stack).setDouble(tag, d)
	}
	
	fun setCompound(stack: ItemStack, tag: String, cmp: NBTTagCompound?) {
		if (!tag.equals("ench", ignoreCase = true)) // not override the enchantments
			getNBT(stack).setTag(tag, cmp)
	}
	
	fun setString(stack: ItemStack, tag: String, s: String) {
		getNBT(stack).setString(tag, s)
	}
	
	fun setList(stack: ItemStack, tag: String, list: NBTTagList?) {
		getNBT(stack).setTag(tag, list)
	}
	
	// GETTERS ///////////////////////////////////////////////////////////////////
	fun verifyExistance(stack: ItemStack?, tag: String): Boolean {
		return stack != null && getNBT(stack).hasKey(tag)
	}
	
	fun getBoolean(stack: ItemStack?, tag: String, defaultExpected: Boolean): Boolean {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getBoolean(tag) else defaultExpected
	}
	
	fun getByte(stack: ItemStack?, tag: String, defaultExpected: Byte): Byte {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getByte(tag) else defaultExpected
	}
	
	fun getByteArray(stack: ItemStack?, tag: String, defaultExpected: ByteArray = ByteArray(0)): ByteArray {
		if (stack == null) return defaultExpected
		return if (verifyExistance(stack, tag)) getNBT(stack).getByteArray(tag) else defaultExpected
	}
	
	fun getShort(stack: ItemStack?, tag: String, defaultExpected: Short): Short {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getShort(tag) else defaultExpected
	}
	
	fun getInt(stack: ItemStack?, tag: String, defaultExpected: Int): Int {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getInteger(tag) else defaultExpected
	}
	
	fun getIntArray(stack: ItemStack?, tag: String, defaultExpected: IntArray = IntArray(0)): IntArray {
		if (stack == null) return defaultExpected
		return if (verifyExistance(stack, tag)) getNBT(stack).getIntArray(tag) else defaultExpected
	}
	
	fun getLong(stack: ItemStack?, tag: String, defaultExpected: Long): Long {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getLong(tag) else defaultExpected
	}
	
	fun getFloat(stack: ItemStack?, tag: String, defaultExpected: Float): Float {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getFloat(tag) else defaultExpected
	}
	
	fun getDouble(stack: ItemStack?, tag: String, defaultExpected: Double): Double {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getDouble(tag) else defaultExpected
	}
	
	fun getCompound(stack: ItemStack?, tag: String): NBTTagCompound {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getCompoundTag(tag) else NBTTagCompound()
	}
	
	/** If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one.  */
	fun getCompound(stack: ItemStack?, tag: String, nullifyOnFail: Boolean): NBTTagCompound? {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getCompoundTag(tag) else if (nullifyOnFail) null else NBTTagCompound()
	}
	
	fun getString(stack: ItemStack?, tag: String, defaultExpected: String): String {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getString(tag) else defaultExpected
	}
	
	fun getList(stack: ItemStack?, tag: String, objtype: Int): NBTTagList {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getTagList(tag, objtype) else NBTTagList()
	}
	
	/** If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one.  */
	fun getList(stack: ItemStack?, tag: String, objtype: Int, nullifyOnFail: Boolean): NBTTagList? {
		return if (verifyExistance(stack, tag)) getNBT(stack!!).getTagList(tag, objtype) else if (nullifyOnFail) null else NBTTagList()
	}
}