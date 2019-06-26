package alexsocol.asjlib.asm

class FieldData(val access: Int, val name: String, val desc: String) {
	
	override fun toString(): String {
		return "$access $desc $name"
	}
}
