package me.danseb.bingo.game;

import lombok.Getter;

/**
 * Teams enum
 * An enum for the game teams, and 2
 * more, SPEC and NONE.
 */
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

    /**
     * Get a team from a String
     * @param name
     * The team you will need
     * @return
     * The team that meets with the string,
     * null if no one meets.
     */
    public static Teams fromName(String name){
        for (Teams team : values()){
            if (team.name().equalsIgnoreCase(name)){
                return team;
            }
        }
         return null;
    }

    /**
     * Get a colored name of the specified
     * team.
     * @return
     * A string with color code.
     */
    public String getColoredName(){
        return this.coloredName;
    }
}
