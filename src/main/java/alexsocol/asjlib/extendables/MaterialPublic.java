package alexsocol.asjlib.extendables;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

// For inline purpose; first call methods from here, then from Material
public class MaterialPublic extends Material {

	public boolean blocker = true, grass = true, liquid = false, opaque = true, solid = true;
	
	public MaterialPublic(MapColor color) {
		super(color);
	}
	
	// BLOCKS WATER
	public boolean blocksMovement() {
		return blocker;
	}
	
	public boolean getCanBlockGrass() {
        return grass;
    }
	
	public boolean isLiquid() {
		return liquid;
	}

	public boolean isOpaque() {
		return opaque;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public Material setAdventureModeExempt() {
        return super.setAdventureModeExempt();
    }
	
	public Material setBurning() {
        return super.setBurning();
    }
	
	public Material setImmovableMobility() {
        return super.setImmovableMobility();
    }
	
	public MaterialPublic setLiquid() {
		liquid = true;
        return this;
    }
	
	public MaterialPublic setGrass() {
		grass = true;
        return this;
    }
	
	public Material setNoPushMobility() {
        return super.setNoPushMobility();
    }
	
	/** Can be washed away */
	public MaterialPublic setNotBlocker() {
		blocker = false;
        return this;
    }
	
	public MaterialPublic setNotOpaque() {
		opaque = false;
		return this;
	}
	
	public MaterialPublic setNotSolid() {
		solid = false;
        return this;
    }
	
	public Material setRequiresTool() {
        return super.setRequiresTool();
    }
}
