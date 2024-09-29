package dev.upscairs.minigameBox.minigames.tntrun;

import dev.upscairs.minigameBox.superclasses.MiniGame;
import dev.upscairs.minigameBox.superclasses.MinigameArena;

public class TntRunGame extends MiniGame {

    public TntRunGame(MinigameArena arena) {
        super(arena);
    }

    @Override
    public void startGameFinal(boolean force) {
        getArena().regenerateArena();
        getArena().movePlayersIn();
        super.startGameFinal(force);
    }

    

    
    













}
