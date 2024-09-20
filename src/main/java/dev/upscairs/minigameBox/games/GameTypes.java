package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.guis.ArenaEditGui;
import dev.upscairs.minigameBox.guis.SpleefArenaEditGui;

import java.util.ArrayList;

public enum GameTypes {
    UNDEFINED("Undefined", MinigameArena.class, MiniGame.class, ArenaEditGui.class, new String[]{"8", "10", "20", "10", "false", "false", "true", "STONE", "A default arena."}),
    SPLEEF("Spleef", SpleefArena.class, SpleefGame.class, SpleefArenaEditGui.class, new String[]{"2", "2", "0", "10", "true", "false", "true", "SHEARS", "A spleef arena.", "1", "WHITE_WOOL"});

    private final String name;
    private final Class<? extends MinigameArena> arenaClass;
    private final Class<? extends MiniGame> gameClass;
    private final Class<? extends ArenaEditGui> editGuiClass;
    private final String[] defaultArgs;

    GameTypes(String name, Class<? extends MinigameArena> arenaClass, Class<? extends MiniGame> gameClass, Class<? extends ArenaEditGui> editGuiClass, String[] defaultArgs) {
        this.name = name;
        this.arenaClass = arenaClass;
        this.gameClass = gameClass;
        this.editGuiClass = editGuiClass;
        this.defaultArgs = defaultArgs;
    }

    public String getName() {
        return name;
    }

    public Class<? extends MinigameArena> getArenaClass() {
        return arenaClass;
    }

    public Class<? extends MiniGame> getGameClass() {
        return gameClass;
    }

    public Class<? extends ArenaEditGui> getEditGuiClass() {
        return editGuiClass;
    }

    public String[] getDefaultArgs() {
        return defaultArgs;
    }

    public static GameTypes getFromName(String name) {
        for (GameTypes gameType : GameTypes.values()) {
            if (gameType.getName().equals(name)) {
                return gameType;
            }
        }
        return null;
    }

    public static GameTypes getFromGameClass(Class<? extends MiniGame> gameClass) {
        for (GameTypes gameType : GameTypes.values()) {
            if (gameType.getGameClass().equals(gameClass)) {
                return gameType;
            }
        }
        return null;
    }

    public static GameTypes getFromArenaClass(Class<? extends MinigameArena> arenaClass) {
        for (GameTypes gameType : GameTypes.values()) {
            if (gameType.getArenaClass().equals(arenaClass)) {
                return gameType;
            }
        }
        return null;
    }

    public static boolean nameExists(String name) {
        if(getFromName(name) != null) {
            return true;
        }
        return false;
    }

}
