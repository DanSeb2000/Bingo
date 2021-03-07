package me.danseb.bingo.game;

public enum Teams {
    RED, YELLOW, GREEN, BLUE, SPEC, NONE;

    public static Teams fromName(String name){
        for (Teams team : values()){
            if (team.name().equalsIgnoreCase(name)){
                return team;
            }
        }
         return null;
    }
}
