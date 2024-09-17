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
        setGameRunning(true);
        getArena().regenerateArena();
        getArena().movePlayersIn();
        //Arena needs default protection
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                for(Player p : getArena().getIngamePlayers()) {
                    p.setMetadata("GameName", new FixedMetadataValue(getPlugin(), getArena().getName()));
                }
            }
        }, getArena().getSetupTimeSec()*20L);

    }

    

    
    













}
