package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.games.MiniGame;

import java.util.HashMap;

public class GameRegister {

    //Maps arena Name to Instance
    private static HashMap<String, MiniGame> games = new HashMap<>();

    public static void loadGames() {
        //Read arena settings from file
        //Create arena
        //create game instance with arena instance
        //Put in map

    }

    public static boolean gameExists(String name) {
        return games.containsKey(name);
    }

    public static MiniGame getGame(String name) {
        return games.get(name);
    }

    public static void shutdownGames() {
        for(MiniGame game : games.values()) {
            game.endGame(false);
        }
    }

}
