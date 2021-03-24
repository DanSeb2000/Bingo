package me.danseb.bingo.game;

import lombok.Getter;
import me.danseb.bingo.utils.Language;

/**
 * Teams enum
 * An enum for the game teams, and 2 more, SPEC and NONE.
 */
@Getter
public enum Teams {
    RED("&c", Language.RED.getMessage()),
    YELLOW("&e", Language.YELLOW.getMessage()),
    GREEN("&2", Language.GREEN.getMessage()),
    BLUE("&9", Language.BLUE.getMessage()),
    SPEC("&7", Language.SPEC.getMessage()),
    NONE("", "");

    private final String colored;
    private final String color;
    private final String team;

    Teams(String colored, String team) {
        this.colored = colored.replace("&", "§")+team;
        this.color = colored.replace("&", "§");
        this.team = team;
    }

    /**
     * Get a team from a String
     * @param name The team you will need
     * @return The team that meets with the string, null if no one meets.
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
     * Get a colored name of the specified team.
     * @return A string with color code.
     */
    public String getColoredName(){
        return this.colored;
    }

    /**
     * Get the color of the specified team.
     * @return A string with the color.
     */
    public String getTeamColor(){
        return this.color;
    }
}
