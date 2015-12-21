package iCraft.core;

import iCraft.core.item.ItemiCraft;
import iCraft.core.network.MessageConfigSync;
import iCraft.core.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import java.util.Arrays;
import java.util.List;

public class CommonPlayerTracker
{
    public CommonPlayerTracker()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        if (!event.player.worldObj.isRemote)
        {
            NetworkHandler.sendTo(new MessageConfigSync(), (EntityPlayerMP) event.player);
            ICraft.logger.info("Sent config to '" + event.player.getDisplayName() + ".'");
        }

        List<ItemStack> itemStacks = Arrays.asList(event.player.inventory.mainInventory);
        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
            {
                if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("called"))
                    itemStack.getTagCompound().setInteger("called", 0);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event)
    {
        List<ItemStack> itemStacks = Arrays.asList(event.player.inventory.mainInventory);
        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
            {
                if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") != 0 && itemStack.getTagCompound().hasKey("isCalling"))
                {
                    for (EntityPlayerMP players : FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList)
                    {
                        if (players.getCommandSenderName().equals((itemStack.getTagCompound().getBoolean("isCalling") ? itemStack.getTagCompound().getString("calledPlayer") : itemStack.getTagCompound().getString("callingPlayer"))))
                        {
                            List<ItemStack> stacks = Arrays.asList(players.inventory.mainInventory);
                            for (ItemStack stack : stacks)
                            {
                                if (stack != null && stack.getItem() instanceof ItemiCraft)
                                {
                                    ItemiCraft iCraft = (ItemiCraft) stack.getItem();
                                    if (stack.getTagCompound() != null && iCraft.getNumber(stack) == (itemStack.getTagCompound().getBoolean("isCalling") ? itemStack.getTagCompound().getInteger("calledNumber") : itemStack.getTagCompound().getInteger("callingNumber")))
                                    {
                                        stack.getTagCompound().setInteger("called", 0);
                                        players.closeScreen();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}