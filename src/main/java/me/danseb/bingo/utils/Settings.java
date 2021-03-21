package me.danseb.bingo.utils;

import me.danseb.bingo.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Setting enum
 *
 * You can freely change this in the config.yml and
 * through some commands.
 */
public enum Settings {
    DIFFICULTY("Game.Difficulty", 1),
    FULLCARD("Game.Fullcard", false),
    GAME_TIME("Game.EndTime", "25:00"),
    WORLD_SPAWN("World.Spawn", "world, 0, 100, 0, 90, 0"),
    OLD_WORLD("World.OldWorld", "0");

    private final String path;
    private Object object;
    private final Core plugin;

    Settings(String path, Object object) {
        this.path = path;
        this.object = object;
        this.plugin = Core.getInstance();
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

    public Location asLocation(){
        String string = String.valueOf(object).replace(" ", "");
        String[] locate = string.split(",");
        return new Location(Bukkit.getWorld(locate[0]),
                Integer.parseInt(locate[1]),
                Integer.parseInt(locate[2]),
                Integer.parseInt(locate[3]),
                Float.parseFloat(locate[4]),
                Float.parseFloat(locate[5]));
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
        FileConfiguration fileConfiguration = plugin.getConfig();
        fileConfiguration.set(this.path, message);
        plugin.saveConfig();
        this.object = message;
    }
}
