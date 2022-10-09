# awt-altgr-patch

This is a simple JavaAgent instrumentation to patch the Java AWT library to work correctly with
Wayland xkb AltGr key combinations.

Some things have changed on [xkeyboard-config](https://gitlab.freedesktop.org/xkeyboard-config/xkeyboard-config/-/issues/321) which is used to provide common xkb configurations, now AltGr
is not the Mode-switch key (0xFF7E) anymore (or Script Switch, or Character Set Switch), 
instead it is now mapped to Mod5 (0x0080).

Obviously, this breaks almost everything that is not xkb aware, which includes applications running under XWayland
or native Wayland applications that relied on behavioral documentation rather than on the actual written
documentation (which is non-existent).

This JavaAgent uses ByteBuddy to patch the AWT library, what it basically does is just add a new
flag check [there](https://github.com/openjdk/jdk/blob/master/src/java.desktop/unix/classes/sun/awt/X11/XWindow.java#L604-L606), like this one below:

```java
if (((state & 0x0080) != 0) ^ (keyCode == KeyEvent.VK_ALT_GRAPH)) {
    modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
}
```

Which extends AWT check to be aware of those xkb changes.

I don't know how extensive was the [xkeyboard-config](https://gitlab.freedesktop.org/xkeyboard-config) changes, 
but I know that they affect the `US, intl., with dead keys` layout and some others, so if your AltGr
keybindings stopped working, it's probably because of those changes.

## Using

It's actually pretty simple, download the latest release from the [releases](https://github.com/JonathanxD/awt-altgr-patch/releases) page and run the JVM with the following option:

```
-javaagent:/path/to/awt-altgr-patch.jar
```

For IntelliJ (and based IDEs), go to `Help > Edit Custom VM Options...` and prepend the line above to the
start of the file, then restart the IDE.

## License

This project is licensed under the 0BSD license. It is so simple that thinking
of a license took more time than writing the code.