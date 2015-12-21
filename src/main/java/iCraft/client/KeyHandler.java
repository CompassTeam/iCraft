package iCraft.client;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class KeyHandler
{
    public KeyBinding[] keyBindings;
    public boolean[] keyDown;
    public boolean[] repeatings;
    public boolean isDummy;

    /**
     * Pass an array of keybindings and a repeat flag for each one
     *
     * @param bindings
     * @param rep
     */
    public KeyHandler(KeyBinding[] bindings, boolean[] rep)
    {
        assert keyBindings.length == repeatings.length : "You need to pass two arrays of identical length";
        keyBindings = bindings;
        repeatings = rep;
        keyDown = new boolean[keyBindings.length];
    }

    /**
     * Register the keys into the system. You will do your own keyboard
     * management elsewhere. No events will fire if you use this method
     *
     * @param  bindings
     */
    public KeyHandler(KeyBinding[] bindings)
    {
        keyBindings = bindings;
        isDummy = true;
    }

    public static boolean isPressed(KeyBinding keyBinding)
    {
        int keyCode = keyBinding.getKeyCode();
        return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
    }

    public KeyBinding[] getKeyBindings ()
    {
        return keyBindings;
    }

    public void keyTick()
    {
        for(int i = 0; i < keyBindings.length; i++)
        {
            KeyBinding keyBinding = keyBindings[i];
            boolean state = keyBinding.isPressed();

            if(state != keyDown[i] || (state && repeatings[i]))
            {
                if(state)
                    keyDown(keyBinding, state == keyDown[i]);
                else
                    keyUp(keyBinding);

                keyDown[i] = state;
            }
        }
    }

    public abstract void keyDown(KeyBinding kb, boolean isRepeat);

    public abstract void keyUp(KeyBinding kb);
}