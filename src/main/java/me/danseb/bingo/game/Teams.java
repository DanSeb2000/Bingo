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

    private final String teamName;

    Teams(String name) {
        this.teamName = name.replace("&", "§");
    }

    public static Teams fromName(String name){
        for (Teams team : values()){
            if (team.name().equalsIgnoreCase(name)){
                return team;
            }
        }
         return null;
    }

    public String getName(){
        return this.teamName;
    }
}
