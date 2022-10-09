package com.github.jonathanxd.awtaltgrpatch;

import net.bytebuddy.asm.Advice;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class XWindowAdvice {
    @Advice.OnMethodExit
    public static void exit(
            @Advice.Argument(0) int state,
            @Advice.Argument(2) int keyCode,
            @Advice.Argument(3) boolean isWheelMouse,
            @Advice.Return(readOnly = false) int modifiers) {

        // 0x0080 is the Mod5 flag, which is the “replacement” for AltGr
        if (((state & 0x0080) != 0) ^ (keyCode == KeyEvent.VK_ALT_GRAPH)) {
            modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
        }
    }
}
