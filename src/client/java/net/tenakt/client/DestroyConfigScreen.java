//package net.tenakt.client;
//
//import io.wispforest.owo.ui.base.BaseOwoScreen;
//import io.wispforest.owo.ui.container.FlowLayout;
//import io.wispforest.owo.ui.component.DiscreteSliderComponent;
//import io.wispforest.owo.ui.component.ButtonComponent;
//import net.minecraft.text.Text;
//import org.jetbrains.annotations.NotNull;
//import net.minecraft.util.Identifier;
//
//public class DestroyConfigScreen extends BaseOwoScreen<FlowLayout> {
//
//    protected DestroyConfigScreen() {
//        // Передаем заголовок экрана (он нужен для истории, на экране его видно не будет)
//        super(Text.literal("Destroy Config"));
//    }
//
//    @Override
//    protected @NotNull Class<FlowLayout> rootLayoutType() {
//        // Указываем, что самый верхний контейнер в XML — это FlowLayout
//        return FlowLayout.class;
//    }
//
//    @Override
//    protected void build(FlowLayout rootComponent) {
//        // 1. Находим наши ползунки по их id из XML
//        DiscreteSliderComponent radiusSlider = rootComponent.childById(DiscreteSliderComponent.class, "radius-slider");
//        DiscreteSliderComponent heightUpSlider = rootComponent.childById(DiscreteSliderComponent.class, "height-up-slider");
//        DiscreteSliderComponent heightDownSlider = rootComponent.childById(DiscreteSliderComponent.class, "height-down-slider");
//
//        // 2. Находим кнопку сохранения
//        ButtonComponent saveButton = rootComponent.childById(ButtonComponent.class, "save-button");
//
//        // 3. Задаем значения по умолчанию при открытии (пока просто цифры, позже свяжем с данными)
//        if (radiusSlider != null) radiusSlider.setValue(5);
//        if (heightUpSlider != null) heightUpSlider.setValue(2);
//        if (heightDownSlider != null) heightDownSlider.setValue(2);
//
//        // 4. Вешаем действие на кнопку "Сохранить"
//        if (saveButton != null) {
//            saveButton.onPress(button -> {
//                // Получаем то, что накрутил игрок
//                int radius = (int) radiusSlider.getValue();
//                int heightUp = (int) heightUpSlider.getValue();
//                int heightDown = (int) heightDownSlider.getValue();
//
//                // TODO: Здесь мы будем отправлять пакет на сервер!
//                System.out.println("Сохранено: R=" + radius + ", Up=" + heightUp + ", Down=" + heightDown);
//
//                // Закрываем экран и возвращаем мышку в игру
//                this.close();
//            });
//        }
//    }
//
//    @Override
//    protected @NotNull Identifier getXMLId() {
//        // Указываем точный путь к XML-файлу в ресурсах: assets/chunk-destroyed/owo_ui/destroy_config.xml
//        return Identifier.of("chunk-destroyed", "destroy_config");
//    }
//}