package iCraft.core.entity;

import com.mojang.authlib.GameProfile;
import iCraft.core.ICraft;
import iCraft.core.tile.TilePackingCase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.UUID;

public class EntityPackingCase extends Entity
{
    public ItemStack[] storedItems;
    private ItemStack itemStack;
    private boolean hasLanded;
    private GameProfile profile;

    public EntityPackingCase(World world, ItemStack[] storedItems, ItemStack itemStack, UUID id, String name)
    {
        this(world);
        this.storedItems = storedItems;
        this.itemStack = itemStack;
        hasLanded = false;
        profile = new GameProfile(id, name);
    }

    public EntityPackingCase(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTags)
    {
        final NBTTagList tagList = nbtTags.getTagList("Items", NBT.TAG_COMPOUND);
        storedItems = new ItemStack[1];

        for (int tagCount = 0; tagCount < tagList.tagCount(); ++tagCount)
        {
            final NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
            final int slotID = tagCompound.getByte("Slot") & 255;

            if (slotID < storedItems.length)
                storedItems[slotID] = ItemStack.loadItemStackFromNBT(tagCompound);
        }

        hasLanded = nbtTags.getBoolean("hasLanded");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTags)
    {
        final NBTTagList tagList = new NBTTagList();

        for (int slotCount = 0; slotCount < storedItems.length; ++slotCount)
        {
            if (storedItems[slotCount] != null)
            {
                final NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) slotCount);
                storedItems[slotCount].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        nbtTags.setTag("Items", tagList);
        nbtTags.setBoolean("hasLanded", hasLanded);
    }

    @Override
    public void onUpdate()
    {
        if (!hasLanded)
        {
            if (onGround && !worldObj.isRemote)
            {
                for (int i = 0; i < 100; i++)
                {
                    final int x = MathHelper.floor_double(this.posX);
                    final int y = MathHelper.floor_double(this.posY);
                    final int z = MathHelper.floor_double(this.posZ);

                    Block block = worldObj.getBlock(x, y + i, z);

                    if (block.getMaterial().isReplaceable())
                    {
                        if (placeCase(x, y + i, z))
                        {
                            setDead();
                            return;
                        }
                        else if (storedItems != null)
                        {
                            for (final ItemStack stack : storedItems)
                            {
                                final EntityItem e = new EntityItem(worldObj, posX, posY, posZ, stack);
                                worldObj.spawnEntityInWorld(e);
                            }

                            return;
                        }
                    }
                }

                if (storedItems != null)
                {
                    for (final ItemStack stack : storedItems)
                    {
                        final EntityItem e = new EntityItem(worldObj, posX, posY, posZ, stack);
                        worldObj.spawnEntityInWorld(e);
                    }
                }
            }
            else
                motionY = -0.25;

            moveEntity(0, motionY, 0);
        }
    }

    private boolean placeCase(int x, int y, int z)
    {
        worldObj.setBlock(x, y, z, ICraft.caseBlock, 0, 3);
        worldObj.removeTileEntity(x, y, z);
        final TileEntity te = new TilePackingCase(profile, itemStack);
        worldObj.setTileEntity(x, y, z, te);

        if (te instanceof TilePackingCase && storedItems != null)
        {
            final TilePackingCase packingCase = (TilePackingCase) te;

            packingCase.inventory = new ItemStack[1];
            packingCase.inventory[0] = storedItems[0];

            return true;
        }
        hasLanded = true;

        return true;
    }
}