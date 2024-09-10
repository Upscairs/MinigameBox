package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.creation_and_storing.PendingArenaCreations;
import dev.upscairs.minigameBox.guis.ArenaEditGui;
import dev.upscairs.minigameBox.guis.InteractableGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinigameCommand implements CommandExecutor {

    private MinigameBox plugin;

    public MinigameCommand(MinigameBox plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if(args.length == 0) {
                //Open arena overview window
                return true;
            }

            if(args[0].equalsIgnoreCase("create")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    //Error message
                    return true;
                }

                if(args.length == 3) {
                    PendingArenaCreations.newSetup(p, args[1], args[2]);
                }
                else {
                    //Message
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setpos")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    //Error message
                    return true;
                }

                MinigameArena arena = PendingArenaCreations.giveNextVar(p, p.getLocation());

                if(arena != null) {
                    GameRegister.saveArenaSettings(arena);
                    GameRegister.getGame(arena.getName()).getArena().regenerateArena();
                }

                return true;
            }

            if(args[0].equalsIgnoreCase("cancel")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    //Error message
                    return true;
                }

                PendingArenaCreations.closeSetup(p);
                //Message
                return true;
            }

            if(args[0].equalsIgnoreCase("edit")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    //Error message
                    return true;
                }

                if(args.length == 2) {
                    if(!GameRegister.gameExists(args[1])) {
                        //Error message
                    }
                    else {
                        InteractableGui gui = new ArenaEditGui(plugin, new String[]{p.getUniqueId().toString()});
                        p.openInventory(gui.getInventory());
                    }
                    return true;
                } else {
                    //Error Message
                    return true;
                }

            }

            if(args[0].equalsIgnoreCase("join")) {
                if(args.length == 2) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinEvent(p, args[1]));
                }
                return true;
            }

            //Subcommand not found


        }

        return true;

    }

}
