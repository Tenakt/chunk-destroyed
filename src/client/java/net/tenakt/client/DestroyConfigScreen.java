package net.tenakt.client;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.component.SpacerComponent;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.util.Identifier;
import net.tenakt.MyModInitializer;

public class DestroyConfigScreen extends BaseUIModelScreen<FlowLayout> {

    public DestroyConfigScreen() {
        super(FlowLayout.class, DataSource.asset(Identifier.of("chunk-destroyer", "destroy_config")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        TextBoxComponent radiusInput = rootComponent.childById(TextBoxComponent.class, "radius-input");
        TextBoxComponent upInput = rootComponent.childById(TextBoxComponent.class, "up-input");
        TextBoxComponent downInput = rootComponent.childById(TextBoxComponent.class, "down-input");
        ButtonComponent saveButton = rootComponent.childById(ButtonComponent.class, "save-button");

        if (saveButton != null) {
            rootComponent.removeChild(saveButton);
        }

        if (radiusInput != null) {
            int currentRadius = MyModInitializer.CONFIG.destroyRadius();
            radiusInput.text(String.valueOf(currentRadius));
        }

        if (upInput != null) {
            int currentUpRadius = MyModInitializer.CONFIG.heightUp();
            upInput.text(String.valueOf(currentUpRadius));
        }

        if (downInput != null) {
            int currentDownRadius = MyModInitializer.CONFIG.heightDown();
            downInput.text(String.valueOf(currentDownRadius));
        }


        if (saveButton != null && radiusInput != null) {
            saveButton.onPress(button -> {
                try {
                    // Берем текст из поля ввода и превращаем его в целое число (int)
                    int newRadius = Integer.parseInt(radiusInput.getText());
                    int newUpRadius = Integer.parseInt(upInput.getText());
                    int newDownRadius = Integer.parseInt(downInput.getText());

                    // Записываем новое число в конфиг
                    MyModInitializer.CONFIG.destroyRadius(newRadius);
                    MyModInitializer.CONFIG.heightUp(newUpRadius);
                    MyModInitializer.CONFIG.heightDown(newDownRadius);

                    // Закрываем меню (возвращаемся в игру)
                    this.close();
                } catch (NumberFormatException e) {
                    // Этот блок сработает, если игрок ввел буквы вместо цифр.
                    // Пока просто игнорируем, чтобы игра не крашнулась.
                }
            });
        }

        SpacerComponent spacer = UIComponents.spacer();
        spacer.sizing(
                Sizing.fill(100),
                Sizing.fill(100)
        );
        rootComponent.child(spacer);
        rootComponent.child(saveButton);
    }
}