package net.tenakt;

import io.wispforest.owo.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Modmenu(modId = "chunk-destroyer")
@Config(name = "my-config", wrapperName = "MyConfig")
public class MyConfigModel {

    @RangeConstraint(min = 1,max = 128)
    public int destroyRadius = 16;


}