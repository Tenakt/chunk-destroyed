package net.tenakt;

import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "chunk-destroyer")
@Config(name = "my-config", wrapperName = "MyConfig")
public class MyConfigModel {

    @SectionHeader("general")

    @RangeConstraint(min = 1, max = 128)
    public int destroyRadius = 16;

    @RangeConstraint(min = 1, max = 384)
    public int heightUp = 384;

    @RangeConstraint(min = 1, max = 384)
    public int heightDown = 384;
}