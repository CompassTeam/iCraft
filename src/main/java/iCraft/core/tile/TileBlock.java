package iCraft.core.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileBlock extends TileBase
{
    /**
     * The direction this block is facing.
     */
    public int facing;
    public int clientFacing;

    public void writeToPacket(ByteBuf buf)
    {
        buf.writeInt(facing);
    }

    public void readFromPacket(ByteBuf buf)
    {
        facing = buf.readInt();
        if (clientFacing != facing)
        {
            worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(), getPos().getY(), getPos().getZ());
            worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
            clientFacing = facing;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);

        facing = nbtTags.getInteger("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);

        nbtTags.setInteger("facing", facing);
    }

    public boolean canSetFacing(int facing)
    {
        return facing != 0 && facing != 1;
    }

    public void setFacing(short direction)
    {
        if (canSetFacing(direction))
            facing = direction;

        if (facing != clientFacing || !worldObj.isRemote)
        {
            worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
            clientFacing = facing;
        }
    }
}