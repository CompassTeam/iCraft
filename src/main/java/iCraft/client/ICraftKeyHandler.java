package iCraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ICraftKeyHandler extends KeyHandler
{
    public static boolean canTalk = true;
    public static final String keybindCategory = "iCraft";
    public static KeyBinding voiceMuteKey = new KeyBinding("iCraft " + ICraftUtils.localize("key.mute"), Keyboard.KEY_M, keybindCategory);

    public ICraftKeyHandler()
    {
        super(new KeyBinding[]{voiceMuteKey}, new boolean[]{false, false});

        ClientRegistry.registerKeyBinding(voiceMuteKey);

        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.side == Side.CLIENT)
        {
            if (event.phase == Phase.START)
                keyTick(event.type, false);
            else if (event.phase == Phase.END)
                keyTick(event.type, true);
        }
    }

    @Override
    public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        if (tickEnd && kb.getKeyCode() == voiceMuteKey.getKeyCode())
            canTalk = !canTalk;
    }

    @Override
    public void keyUp(Type types, KeyBinding kb, boolean tickEnd) {}
}