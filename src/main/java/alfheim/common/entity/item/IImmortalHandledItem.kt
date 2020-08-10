package alfheim.common.entity.item

interface IImmortalHandledItem {
	
	fun onEntityItemImmortalUpdate(entity: EntityItemImmortal): Boolean
}
