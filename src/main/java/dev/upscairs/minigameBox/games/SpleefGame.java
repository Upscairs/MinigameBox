package dev.upscairs.minigameBox.games;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.SpleefArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SpleefGame extends MiniGame {

    private SpleefArena arena;
    
    public SpleefGame(MinigameArena arena) {
        super(arena);
    }

    @Override
    public void startGameFinal() {
        setGameRunning(true);
        arena.regenerateArena();
        arena.movePlayersIn();
        //Arena needs default protection
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(Player p : arena.getIngamePlayers()) {
                    p.setMetadata("GameName", new FixedMetadataValue(getPlugin(), arena.getName()));
                }
            }
        }, arena.getSetupTimeSec()*20L);

    }

    

    
    













}
