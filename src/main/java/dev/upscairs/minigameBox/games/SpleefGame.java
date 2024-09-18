package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

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
