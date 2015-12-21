package iCraft.core.block;

import iCraft.core.tile.TilePackingCase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPackingCase extends BlockContainer
{
    public BlockPackingCase()
    {
        super(Material.wood);
        setStepSound(Block.soundTypeWood);
        setBlockUnbreakable(); //Only the person who purchased this will be able to open.
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TilePackingCase te = (TilePackingCase) world.getTileEntity(pos);
        int side = MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int height = Math.round(placer.rotationPitch);
        int change = 3;

        if (te == null)
            return;

        if (te.canSetFacing(0) && te.canSetFacing(1))
        {
            if (height >= 65)
                change = 1;
            else if (height <= -65)
                change = 0;
        }
        if (change != 0 && change != 1)
        {
            switch (side)
            {
                case 0:
                    change = 2;
                    break;
                case 1:
                    change = 5;
                    break;
                case 2:
                    change = 3;
                    break;
                case 3:
                    change = 4;
                    break;
            }
        }

        te.setFacing((short) change);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && !player.isSneaking())
        {
            TilePackingCase te = (TilePackingCase) world.getTileEntity(pos);
            if (te != null)
            {
                if (te.canOpen(player))
                {
                    world.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5D, pos.getZ() + 0.5F, "random.chestopen", 0.5F, (world.rand.nextFloat() * 0.1F) + 0.9F);

                    if (te.inventory[0] != null)
                    {
                        int i = player.inventory.getFirstEmptyStack();
                        if (i != -1)
                        {
                            player.inventory.setInventorySlotContents(i, te.inventory[0]);

                            Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, te.inventory[0]));
                        }
                        else
                        {
                            EntityItem ent = new EntityItem(world, player.posX + 0.5F, player.posY + 0.5F, player.posZ + 0.5F, te.inventory[0]);
                            world.setBlockToAir(pos);
                            world.spawnEntityInWorld(ent);
                        }
                    }
                    world.setBlockToAir(pos);
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TilePackingCase(null, null);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 2;
    }
}