package iCraft.core.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class TilePackingCase extends TileInventory
{
    private GameProfile profile;
    private ItemStack itemStack;

    public TilePackingCase(GameProfile profile, ItemStack itemStack)
    {
        super("PackingCase");
        inventory = new ItemStack[1];
        this.profile = profile;
        this.itemStack = itemStack;
    }

    public boolean canOpen(EntityPlayer player)
    {
        if (profile != null)
        {
            if (player.getGameProfile() != null && profile.equals(player.getGameProfile()))
            {
                for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
                {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if (player.inventory.getStackInSlot(i) != null && stack.getItem() == itemStack.getItem() && stack.stackSize >= itemStack.stackSize && stack.getItemDamage() == itemStack.getItemDamage())
                    {
                        stack.stackSize = stack.stackSize - itemStack.stackSize;
                        if (stack.stackSize <= 0)
                            stack = null;
                        player.inventory.setInventorySlotContents(i, stack);

                        Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                        ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, stack)); // This was really annoying to sync

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return false;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        updateContainingBlockInfo();
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return false;
    }
}