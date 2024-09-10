package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.creation_and_storing.PendingArenaCreations;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.events.custom.PlayerJoinQueueEvent;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveQueueEvent;
import dev.upscairs.minigameBox.guis.ArenaEditGui;
import dev.upscairs.minigameBox.guis.InteractableGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinigameCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if(args.length == 0) {
                //Open arena overview window
                return true;
            }

            if(args[0].equalsIgnoreCase("create")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(args.length >= 3) {
                    PendingArenaCreations.newSetup(p, args[1], args[2]);
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-create-wrong-syntax"));
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setpos")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                MinigameArena arena = PendingArenaCreations.giveNextVar(p, p.getLocation());

                if(arena != null) {
                    GameRegister.saveArenaSettings(arena);
                    GameRegister.getGame(arena.getName()).getArena().regenerateArena();
                }

                return true;
            }

            if(args[0].equalsIgnoreCase("setup-cancel")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                PendingArenaCreations.closeSetup(p);
                p.sendMessage(MessagesConfig.get().getString("managing.success-setup-canceled"));
                return true;
            }

            if(args[0].equalsIgnoreCase("edit")) {

                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(args.length >= 2) {
                    if(!GameRegister.gameExists(args[1])) {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                    }
                    else {
                        InteractableGui gui = new ArenaEditGui(new String[]{p.getUniqueId().toString()});
                        p.openInventory(gui.getInventory());
                    }
                    return true;
                } else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    return true;
                }

            }

            if(args[0].equalsIgnoreCase("join")) {
                if(args.length == 2) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinQueueEvent(p, args[1]));
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveQueueEvent(p));
            }

            //Subcommand not found


        }

        return true;

    }

}
