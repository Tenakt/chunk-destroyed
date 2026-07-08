package net.tenakt;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "chunk-destroyer")
@Config(name = "my-config", wrapperName = "MyConfig")
public class MyConfigModel {
    public String firstOption = "1";
    public int anIntOption = 16;
    public boolean aBooleanToggle = false;

    public Choices anEnumOption = Choices.ANOTHER_CHOICE;

    public enum Choices {
        A_CHOICE, ANOTHER_CHOICE;
    }
    @SectionHeader("someSection")
    public String someLaterOption = "42";

    @Nest
    public ThisIsNested nestedObject = new ThisIsNested();

    public static class ThisIsNested{
        public boolean aNestedValue = false;
        public int anotherNestedValue = 42;
    }

}