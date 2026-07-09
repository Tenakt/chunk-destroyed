package net.tenakt;

import io.wispforest.owo.config.annotation.*;

import java.util.ArrayList;

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

    @RegexConstraint("[a-z]{1,10}")
    public String aStringOption = "matched";

    @RangeConstraint(min = 10,max = 20)
    public int anIntOption2 = 16;

    @RangeConstraint(min = 5.5d,max = 11.3d)
    public double aDoubleOption = 7.5;

    @PredicateConstraint("predicateFunction")
    public List<String> someOption = new ArrayList<>(List.of("1"))
}