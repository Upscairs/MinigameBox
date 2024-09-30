package dev.upscairs.minigameBox.minigames.spleef;

import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.superclasses.MiniGame;

public class SpleefGame extends MiniGame {

    public SpleefGame(MinigameArena arena) {
        super(arena);
    }

    @Override
    public void startGameFinal(boolean force) {
        getArena().regenerateArena();
        getArena().movePlayersIn();
        super.startGameFinal(force);
    }

}
