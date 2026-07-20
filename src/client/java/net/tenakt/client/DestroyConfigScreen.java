package net.tenakt.client;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;

public class DestroyConfigScreen extends BaseUIModelScreen<FlowLayout> {

    public DestroyConfigScreen() {
        super(FlowLayout.class,DataSource.asset(Identifier.of("chunk-destroyer", "destroy_config")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonComponent.class, "the-button").onPress(button -> {
            System.out.println("click");
        });
    }
}