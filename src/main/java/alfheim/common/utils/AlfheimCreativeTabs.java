package alfheim.common.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AlfheimCreativeTabs extends CreativeTabs{

	private Item IconItem;

	public AlfheimCreativeTabs(String label){
		super(label);
	}

	public void setIconItemIndex(Item iconItem){
		this.IconItem = iconItem;
	}

	@Override
	public String getTranslatedTabLabel(){
		return this.getTabLabel();
	}

	@Override
	public Item getTabIconItem(){
		return this.IconItem;
	}
}
