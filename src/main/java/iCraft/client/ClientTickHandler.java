package iCraft.client;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientTickHandler
{
    public static Minecraft mc = FMLClientHandler.instance().getClient();
    private boolean hasNotified = false;
    public static PositionedSoundRecord phoneRing = PositionedSoundRecord.create(new ResourceLocation("iCraft:ringPhone"), 1.0F);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == Phase.START)
            tickStart();
    }

    public void tickStart()
    {
        if (!hasNotified && mc.theWorld != null && ICraft.newestVersion != null)
        {
            ICraftClientUtils.checkUpdates(mc.thePlayer);
            hasNotified = true;
        }
        if (mc.theWorld != null)
        {
            List<ItemStack> itemStacks = Arrays.asList(mc.thePlayer.inventory.mainInventory);
            for (ItemStack itemStack : itemStacks)
            {
                if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
                {
                    if (itemStack.getTagCompound() != null) {
                        if (itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") == 1 && itemStack.getTagCompound().hasKey("isCalling") && !itemStack.getTagCompound().getBoolean("isCalling"))
                        {
                            if (!mc.getSoundHandler().isSoundPlaying(phoneRing))
                                mc.getSoundHandler().playSound(phoneRing);
                        }
                    }
                }
            }
        }
    }
}