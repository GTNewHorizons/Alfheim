package alfheim.common.block;

import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.client.render.block.RenderBlockElvenGrass;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockElvenGrass extends Block implements IGrowable, ILexiconable {

    @SideOnly(Side.CLIENT)
    public IIcon top;
    @SideOnly(Side.CLIENT)
    public IIcon side;
    @SideOnly(Side.CLIENT)
    public IIcon snowySide;
    @SideOnly(Side.CLIENT)
    public IIcon overlay;
    
    // why not...
	public static Block grassCrutch = new ElvenGrassCrutch();
    
    public BlockElvenGrass() {
        super(Material.grass);
        this.setBlockName("ElvenGrass");
        this.setBlockTextureName(ModInfo.MODID + ":ElvenGrass");
        this.setCreativeTab(AlfheimCore.alfheimTab);
        this.setHardness(0.6F);
        this.setTickRandomly(true);
        this.setStepSound(soundTypeGrass);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.top : (side == 0 ? Blocks.dirt.getBlockTextureFromSide(side) : this.blockIcon);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getCurrentEquippedItem();
    	if (stack != null && stack.getItem() instanceof ItemHoe) {
    		if (!player.canPlayerEdit(x, y, z, side, stack)) return false;
            else {
                UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
                
                if (MinecraftForge.EVENT_BUS.post(event)) return false;

                if (event.getResult() == Result.ALLOW) {
                    stack.damageItem(1, player);
                    return true;
                }

                Block block = world.getBlock(x, y, z);

                if (side != 0 && world.isAirBlock(x, y + 1, z)) {
                    Block block1 = Blocks.farmland;
                    world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

                    if (world.isRemote) return true;
                    else {
                        world.setBlock(x, y, z, block1);
                        stack.damageItem(1, player);
                        return true;
                    }
                }
                else return false;
            }
    	}
    	return false;
    }
    
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
    	EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
    	if (plantable instanceof BlockBush) return true;
    	switch (plantType) {
            case Desert: return true;
            case Nether: return false;
            case Crop:   return false;
            case Cave:   return true;
            case Plains: return true;
            case Water:  return false;
            case Beach:
                boolean hasWater = (world.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z + 1).getMaterial() == Material.water);
                return hasWater;
        }
    	
    	return false;
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
                world.setBlock(x, y, z, Blocks.dirt);
            } else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                for (int l = 0; l < 4; ++l) {
                    int i1 = x + rand.nextInt(3) - 1;
                    int j1 = y + rand.nextInt(5) - 3;
                    int k1 = z + rand.nextInt(3) - 1;
                    Block block = world.getBlock(i1, j1 + 1, k1);

                    if (world.getBlock(i1, j1, k1) == Blocks.dirt && world.getBlockMetadata(i1, j1, k1) == 0 && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
                        world.setBlock(i1, j1, k1, AlfheimBlocks.elvenGrass);
                    }
                }
            }
        }
    }

    public Item getItemDropped(int meta, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(0, rand, fortune);
    }

    public boolean func_149851_a(World world, int x, int y, int z, boolean canGrow) {
        return true;
    }

    public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side == 1) {
            return this.top;
        } else if (side == 0) {
            return Blocks.dirt.getBlockTextureFromSide(side);
        } else {
            Material material = world.getBlock(x, y + 1, z).getMaterial();
            return material != Material.snow && material != Material.craftedSnow ? this.blockIcon : this.snowySide;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.top = reg.registerIcon(this.getTextureName());
        this.overlay = reg.registerIcon(this.getTextureName() + "Overlay");
        this.snowySide = reg.registerIcon(this.getTextureName() + "Snowy");
        this.blockIcon = this.side = reg.registerIcon(this.getTextureName() + "Side");
    }

    public void func_149853_b(World world, Random rand, int x, int y, int z) {
        int l = 0;

        while (l < 128) {
            int i1 = x;
            int j1 = y + 1;
            int k1 = z;
            int l1 = 0;

            while (true) {
                if (l1 < l / 16) {
                    i1 += rand.nextInt(3) - 1;
                    j1 += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2;
                    k1 += rand.nextInt(3) - 1;

                    if (world.getBlock(i1, j1 - 1, k1) == AlfheimBlocks.elvenGrass && !world.getBlock(i1, j1, k1).isNormalCube()) {
                        ++l1;
                        continue;
                    }
                } else if (world.getBlock(i1, j1, k1).getMaterial() == Material.air) {
                    if (rand.nextInt(8) != 0) {
                        if (Blocks.tallgrass.canBlockStay(world, i1, j1, k1)) {
                            world.setBlock(i1, j1, k1, Blocks.tallgrass, 1, 3);
                        }
                    } else {
                        world.getBiomeGenForCoords(i1, k1).plantFlower(world, rand, i1, j1, k1);
                    }
                }

                ++l;
                break;
            }
        }
    }
    
    @Override
    public int getRenderType() {
    	return RenderBlockElvenGrass.elvenGrassRendererID;
    }
	
    @Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
    	return (side != 1) ? super.shouldSideBeRendered(access, x, y, z, side) : false;
    }
    
    
    /**
     * This is here because Alfheim team is too stupid to fix colored grass rendering
     * Do not ever make such a bad code...
     * */
    public static class ElvenGrassCrutch extends Block {
        
    	public ElvenGrassCrutch() {
    		super(Material.grass);
    		this.setBlockTextureName(ModInfo.MODID + ":ElvenGrass");
    	}
    	
    	/**
         * Gets the block's texture. Args: side, meta
         */
        @SideOnly(Side.CLIENT)
        public IIcon getIcon(int side, int meta) {
            return side == 1 ? ((BlockElvenGrass)AlfheimBlocks.elvenGrass).top : (side == 0 ? null : ((BlockElvenGrass)AlfheimBlocks.elvenGrass).overlay);
        }
        
        @SideOnly(Side.CLIENT)
        public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
            if (side == 1) {
                return ((BlockElvenGrass)AlfheimBlocks.elvenGrass).top;
            } else if (side == 0) {
                return null;
            } else {
                Material material = world.getBlock(x, y + 1, z).getMaterial();
                return material != Material.snow && material != Material.craftedSnow ? ((BlockElvenGrass)AlfheimBlocks.elvenGrass).overlay : null;
            }
        }
        
        @SideOnly(Side.CLIENT)
        public void registerBlockIcons(IIconRegister reg) {
            this.blockIcon = ((BlockElvenGrass)AlfheimBlocks.elvenGrass).side;
        }

        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            double d0 = 0.5D;
            double d1 = 1.0D;
            return ColorizerGrass.getGrassColor(d0, d1);
        }

        /**
         * Returns the color this block should be rendered. Used by leaves.
         */
        @SideOnly(Side.CLIENT)
        public int getRenderColor(int meta) {
            return this.getBlockColor();
        }

        /**
         * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
         * when first determining what to render.
         */
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
            int l = 0;
            int i1 = 0;
            int j1 = 0;

            for (int k1 = -1; k1 <= 1; ++k1) {
                for (int l1 = -1; l1 <= 1; ++l1) {
                    int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor(x + l1, y, z + k1);
                    l += (i2 & 16711680) >> 16;
                    i1 += (i2 & 65280) >> 8;
                    j1 += i2 & 255;
                }
            }

            return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
        }

        @SideOnly(Side.CLIENT)
        public static IIcon getIconSideOverlay() {
            return ((BlockElvenGrass)AlfheimBlocks.elvenGrass).overlay;
        }
    	
        @Override
    	@SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        	return (side > 0) ? super.shouldSideBeRendered(access, x, y, z, side) : false;
        }
    }

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.worldgen;
	}
}