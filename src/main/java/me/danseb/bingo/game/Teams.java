package me.danseb.bingo.game;

import lombok.Getter;

@Getter
public enum Teams {
    RED("&cRed"),
    YELLOW("&eYellow"),
    GREEN("&2Green"),
    BLUE("&9Blue"),
    SPEC(""),
    NONE("");

    private final String coloredName;

    Teams(String name) {
        this.coloredName = name.replace("&", "§");
    }

    public static Teams fromName(String name){
        for (Teams team : values()){
            if (team.name().equalsIgnoreCase(name)){
                return team;
            }
        }
         return null;
    }

    public String getColoredName(){
        return this.coloredName;
    }
}
