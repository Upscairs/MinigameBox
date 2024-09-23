package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.arenas.MinigameArena;

public class SpleefGame extends MiniGame {

    public SpleefGame(MinigameArena arena) {
        super(arena);
    }

    @Override
    public void startGameFinal() {
        getArena().regenerateArena();
        getArena().movePlayersIn();
        super.startGameFinal();
    }

    

    
    













}
