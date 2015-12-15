package iCraft.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.tile.TilePackingCase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {}

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemstack)
    {
        TilePackingCase te = (TilePackingCase) world.getTileEntity(x, y, z);
        int side = MathHelper.floor_double((entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int height = Math.round(entityliving.rotationPitch);
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float hitX, float hitY, float hiZ)
    {
        if (!world.isRemote && !player.isSneaking())
        {
            TilePackingCase te = (TilePackingCase) world.getTileEntity(x, y, z);
            if (te != null)
            {
                if (te.canOpen(player))
                {
                    world.playSoundEffect(x + 0.5F, y + 0.5D, z + 0.5F, "random.chestopen", 0.5F, (world.rand.nextFloat() * 0.1F) + 0.9F);

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
                            world.setBlockToAir(x, y, z);
                            world.spawnEntityInWorld(ent);
                        }
                    }
                    world.setBlockToAir(x, y, z);
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
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
}