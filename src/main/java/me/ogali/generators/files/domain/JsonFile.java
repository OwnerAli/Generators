package me.ogali.generators.files.domain;

import de.leonhard.storage.Json;

public abstract class JsonFile<T> extends Json {

    public JsonFile(String fileName) {
        super(fileName, "plugins/Generators");
    }

    public abstract void saveToFile(T object);

    public abstract void loadFromFile();

}