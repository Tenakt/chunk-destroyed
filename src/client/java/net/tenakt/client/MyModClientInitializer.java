package net.tenakt.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import net.tenakt.MyModInitializer;

public class MyModClientInitializer implements ClientModInitializer {

    private static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chunk-destroyer.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeyBinding.Category.create(Identifier.of("category.chunk-destroyer"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new DestroyConfigScreen());
                }
            }
        });
    }
}