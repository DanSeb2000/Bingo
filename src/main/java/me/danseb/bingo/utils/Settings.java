package me.danseb.bingo.utils;

import me.danseb.bingo.Core;
import org.bukkit.configuration.file.FileConfiguration;

public enum Settings {
    DIFFICULTY("Game.Difficulty", 0),
    FULLCARD("Game.Fullcard", false),
    GAME_TIME("Game.EndTime", "25:00");

    private final String path;
    private Object object;

    Settings(String path, Object object) {
        this.path = path;
        this.object = object;
    }

    public boolean asBoolean() {
        return (Boolean) object;
    }

    public Double asDouble() {
        return (Double) object;
    }

    public String asString() {
        return String.valueOf(object);
    }

    public int asInt() {
        return (int) object;
    }

    public static void load() {
        FileConfiguration fileConfiguration = Core.getInstance().getConfig();
        for (Settings value : values()) {
            Object object = value.getObject();
            if (fileConfiguration.contains(value.path)) {
                value.setObject(fileConfiguration.get(value.path, object));
            } else {
                fileConfiguration.set(value.path, object);
            }
        }
        Core.getInstance().saveConfig();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object message) {
        this.object = message;
    }
}
