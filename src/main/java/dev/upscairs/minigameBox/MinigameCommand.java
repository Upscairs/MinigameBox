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
import dev.upscairs.minigameBox.guis.ArenaListGui;
import dev.upscairs.minigameBox.guis.InteractableGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MinigameCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if(args.length == 0 || args[0].equals("list")) {
                ArrayList<MinigameArena> arenas = new ArrayList<>();
                for(String key : GameRegister.getGames().keySet()) {
                    if(GameRegister.getGame(key).getArena().isVisible()) {
                        arenas.add(GameRegister.getGame(key).getArena());
                    }
                }
                p.openInventory(new ArenaListGui(new String[]{p.getUniqueId().toString(), "0"}, arenas).getInventory());
                return true;
            }
            else if(args[0].equalsIgnoreCase("join")) {
                for(int i = 2; i < args.length; i++) {
                    args[1] = args[1] + " " + args[i];
                }
                //Checking if game exists
                if(!GameRegister.gameExists(args[1])) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                    return true;
                }
                //calling JoinQueueEvent, handling there
                if(args.length >= 2) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinQueueEvent(p, args[1]));
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveQueueEvent(p));
                return true;
            }

            if(p.hasPermission("minigamebox.manage")) {

                /*

                +++++ ADMIN ZONE +++++

                */

                if(args[0].equalsIgnoreCase("create")) {

                    //All args given -> new setup attempt
                    if(args.length >= 3) {
                        PendingArenaCreations.newSetup(p, args[1], combineArgsFrom(2, args));
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-create-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("setpos")) {

                    //giving next positions to setup
                    MinigameArena arena = PendingArenaCreations.giveNextVar(p, p.getLocation());

                    //If setup done. Save arena and set blocks
                    if(arena != null) {
                        GameRegister.saveArenaSettings(arena);
                        GameRegister.getGame(arena.getName()).getArena().regenerateArena();
                    }

                    return true;
                }
                else if(args[0].equalsIgnoreCase("setup-cancel")) {

                    //Calling setup cancel
                    PendingArenaCreations.closeSetup(p);
                    p.sendMessage(MessagesConfig.get().getString("managing.success-setup-canceled"));
                    return true;
                }
                else if(args[0].equalsIgnoreCase("edit")) {

                    //Checking if enough args
                    if(args.length >= 2) {

                        String gameName = combineArgsFrom(1, args);

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

                    } else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;

                }
                else if(args[0].equalsIgnoreCase("edit-input")) {

                    if(args.length >= 2) {
                        PendingArenaEdits.giveArg(p, combineArgsFrom(1, args));
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("edit-cancel")) {
                    PendingArenaEdits.removePlayer(p);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("delete")) {

                    if(args.length >= 2) {
                        //Checking if game exists
                        if(!GameRegister.gameExists(combineArgsFrom(1, args))) {
                            p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                            return true;
                        }
                        GameRegister.deleteArena(combineArgsFrom(1, args));
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("refresh")) {

                    if(args.length >= 2) {

                        String gameName = combineArgsFrom(1, args);

                        //Checking if game exists
                        if(!GameRegister.gameExists(gameName)) {
                            p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                            return true;
                        }

                        if(!GameRegister.getGame(gameName).isGameRunning()) {
                            //TODO p.sendMessage(MessagesConfig.get().getString("managing.error-game-running"));
                            return true;
                        }
                        GameRegister.reloadGame(gameName, GameTypes.getFromGameClass(GameRegister.getGame(gameName).getClass()));
                        GameRegister.getGame(gameName).getArena().regenerateArena();
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("start")) {

                    if(args.length >= 2) {
                        //Checking if game exists
                        if(!GameRegister.gameExists(combineArgsFrom(1, args))) {
                            p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                            return true;
                        }
                        GameRegister.getGame(combineArgsFrom(1, args)).startGameCountdown();
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("stop")) {

                    if(args.length >= 2) {

                        String gameName = combineArgsFrom(1, args);

                        //Checking if game exists
                        if(!GameRegister.gameExists(gameName)) {
                            p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                            return true;
                        }
                        GameRegister.getGame(gameName).endGame(true);
                    }
                    else {
                        p.sendMessage(MessagesConfig.get().getString("managing.error-edit-wrong-syntax"));
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("debug")) {
                    p.sendMessage(p.hasMetadata("GameName") + "");
                    return true;
                }
            }
            //TODO Subcommand not found

        }
        else {
            //TODO send from console
        }

        return true;

    }

    private String combineArgsFrom(int index, String[] args) {

        StringBuilder out = new StringBuilder();

        for(int i = index; i < args.length; i++) {
            out.append(args[i]);
        }

        return out.toString();

    }

}
