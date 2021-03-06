package customblockfabricator.block;

import javax.annotation.Nullable;

import customblockfabricator.tiles.AbstractTileEntity;
import customblockfabricator.tiles.FabricatorTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Fabricator extends AbstractTileEntity<FabricatorTile> {
  public Fabricator() {
    super(Material.ROCK, "fabricator");
    setHardness(3.5f);
    setResistance(17.5f);
    setLightOpacity(2);
    translucent = true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public Fabricator setCreativeTab(CreativeTabs tab) {
    super.setCreativeTab(tab);
    return this;
  }

  @Override
  public Class<FabricatorTile> getTileEntityClass() {
    return FabricatorTile.class;
  }

  @Nullable
  @Override
  public FabricatorTile createTileEntity(World world, IBlockState state) {
    return new FabricatorTile();
  }

  @Override
  public void onBlockPlacedBy(
      World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
  {
    super.onBlockPlacedBy(world, pos, state, entity, stack);
    TileEntity tile = world.getTileEntity(pos);
    if (tile instanceof FabricatorTile) {
      NBTTagCompound compound = stack.getSubCompound("BlockEntityTag");
      ((FabricatorTile)tile).handleUpdateTag(compound);
    }
  }

  @Override
  public void getDrops(
    NonNullList<ItemStack> drops,
    IBlockAccess world,
    BlockPos blockPos,
    IBlockState blockState,
    int fortune
  ) {

  }

  @Override
  public void breakBlock(
    World world,
    BlockPos blockPos,
    IBlockState blockState
  ) {
    TileEntity tile = world.getTileEntity(blockPos);
    ItemStack stack = new ItemStack(blockState.getBlock(), 1);
    NBTTagCompound compound = new NBTTagCompound();
    tile.writeToNBT(compound);
    NBTTagCompound finalCompound = new NBTTagCompound();
    finalCompound.setTag("BlockEntityTag", compound);
    stack.setTagCompound(finalCompound);

    spawnAsEntity(world, blockPos, stack);

    super.breakBlock(world, blockPos, blockState);
  }

  @Override
	public ItemStack getPickBlock(
    IBlockState blockState,
    RayTraceResult target,
    World world,
    BlockPos pos,
    EntityPlayer player
  ) {
    TileEntity tile = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(blockState.getBlock(), 1);
    NBTTagCompound compound = new NBTTagCompound();
    tile.writeToNBT(compound);
    NBTTagCompound finalCompound = new NBTTagCompound();
    finalCompound.setTag("BlockEntityTag", compound);
    stack.setTagCompound(finalCompound);
		return stack;
  }
}
