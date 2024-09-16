package dev.upscairs.minigameBox;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.creation_and_storing.PendingArenaCreations;
import dev.upscairs.minigameBox.arenas.creation_and_storing.PendingArenaEdits;
import dev.upscairs.minigameBox.config.MessagesConfig;
import dev.upscairs.minigameBox.events.custom.PlayerJoinQueueEvent;
import dev.upscairs.minigameBox.events.custom.PlayerLeaveQueueEvent;
import dev.upscairs.minigameBox.games.GameTypes;
import dev.upscairs.minigameBox.guis.ArenaEditGui;
import dev.upscairs.minigameBox.guis.InteractableGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;

public class MinigameCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if(args.length == 0) {
                //Open arena overview window
                return true;
            }

            if(args[0].equalsIgnoreCase("create")) {

                //Permission check
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                //All args given -> new setup attempt
                if(args.length >= 3) {
                    for(int i = 3; i < args.length; i++) {
                        args[2] = args[2] + " " + args[i];
                    }
                    PendingArenaCreations.newSetup(p, args[1], args[2]);
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-create-wrong-syntax"));
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("setpos")) {

                //Permission
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                //giving next positions to setup
                MinigameArena arena = PendingArenaCreations.giveNextVar(p, p.getLocation());

                //If setup done. Save arena and set blocks
                if(arena != null) {
                    GameRegister.saveArenaSettings(arena);
                    GameRegister.getGame(arena.getName()).getArena().regenerateArena();
                }

                return true;
            }

            if(args[0].equalsIgnoreCase("setup-cancel")) {

                //Permission
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                //Calling setuo cancel
                PendingArenaCreations.closeSetup(p);
                p.sendMessage(MessagesConfig.get().getString("managing.success-setup-canceled"));
                return true;
            }

            if(args[0].equalsIgnoreCase("edit")) {

                //Permission
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                //Checking if enough args
                if(args.length >= 2) {

                    for(int i = 2; i < args.length; i++) {
                        args[1] = args[1] + " " + args[i];
                    }

                    String gameName = args[1];

                    //Checking if game exists
                    if(!GameRegister.gameExists(gameName)) {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                        return true;
                    }


                    ArenaEditGui gui = null;
                    GameTypes gameType = GameTypes.getFromGameClass(GameRegister.getGame(gameName).getClass());

                    try {
                        gui = gameType.getEditGuiClass().getDeclaredConstructor(String[].class).newInstance((Object) new String[]{p.getUniqueId().toString(), gameName});
                    } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    //InteractableGui gui = new ArenaEditGui(new String[]{p.getUniqueId().toString()});
                    p.openInventory(gui.getInventory());

                    return true;

                } else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    return true;
                }

            }

            if(args[0].equalsIgnoreCase("edit-input")) {
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(args.length >= 2) {
                    for(int i = 2; i < args.length; i++) {
                        args[1] = args[1] + " " + args[i];
                    }
                    PendingArenaEdits.giveArg(p, args[1]);
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                }
            }

            if(args[0].equalsIgnoreCase("edit-cancel")) {
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }
                PendingArenaEdits.removePlayer(p);
            }

            if(args[0].equalsIgnoreCase("delete")) {
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(args.length >= 2) {
                    for(int i = 2; i < args.length; i++) {
                        args[1] = args[1] + " " + args[i];
                    }
                    GameRegister.deleteArena(args[1]);
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                }
            }

            if(args[0].equalsIgnoreCase("refresh")) {
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(!GameRegister.getGame(args[1]).isGameRunning()) {
                    //TODO p.sendMessage(MessagesConfig.get().getString("managing.error-game-running"));
                    return true;
                }

                if(args.length >= 2) {
                    for(int i = 2; i < args.length; i++) {
                        args[1] = args[1] + " " + args[i];
                    }
                    GameRegister.reloadGame(args[1], GameTypes.getFromGameClass(GameRegister.getGame(args[1]).getClass()));
                    GameRegister.getGame(args[1]).getArena().regenerateArena();
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                }
            }

            if(args[0].equalsIgnoreCase("stop")) {
                if(!p.hasPermission("minigamebox.manage")) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-no-permission"));
                    return true;
                }

                if(args.length >= 2) {
                    for(int i = 2; i < args.length; i++) {
                        args[1] = args[1] + " " + args[i];
                    }
                    GameRegister.getGame(args[1]).endGame(true);
                }
                else {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                }
            }

            if(args[0].equalsIgnoreCase("join")) {
                //calling JoinQueueEvent, handling there
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
