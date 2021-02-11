package alexsocol.asjlib;

public class ASJBitwiseHelper {
	
	public static int invertBit(int i, int pos) {
		return i ^ 1 << pos;
	}
	
	/**
	 * Gets how many bits is used (farthest non-zero bit position starting from 1)
	 * Usable for writeToInt method's bits parameter
	 */
	public static int getSize(int i) {
		int pos = 0;
		for (int j = 0; i < 32; i++) if (getBit(i, j)) pos = j + 1;
		return pos;
	}
	
	public static boolean getBit(int i, int pos) {
		return ((i >> pos) & 1) != 0;
	}
	
	public static int writeToInt(int i, int value, int offset, int bits) {
		for (int j = offset, k = 0; j <= bits + 1; j++, k++) i = setBit(i, j, getBit(value, k));
		return i;
	}
	
	public static int setBit(int i, int pos, boolean value) {
		return value ? i | 1 << pos : i & ~(1 << pos);
	}
	
	public static int readFromInt(int i, int offset, int bits) {
		int value = 0;
		for (int j = offset, k = 0; j <= bits + 1; j++, k++) value = setBit(value, k, getBit(i, j));
		return value;
	}
}