package com.kral1k.krabot.utils.jsonconfig;

import java.nio.file.Path;

public interface JsonConfig {
    static <T extends JsonConfig> JsonConfigSaver<T> load(Class<T> class_, Path path) {
        return new JsonConfigSaver<>(class_, path).load();
    }
}
