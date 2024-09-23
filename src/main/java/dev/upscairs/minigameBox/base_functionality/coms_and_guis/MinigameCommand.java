package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing.PendingArenaCreations;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing.PendingArenaEdits;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.superclasses.guis.ArenaEditGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

                String gameName = combineArgsFrom(1, args);

                //Checking if game exists
                if(!GameRegister.gameExists(gameName)) {
                    p.sendMessage(MessagesConfig.get().getString("managing.error-game-not-found"));
                    return true;
                }
                if(args.length >= 2) {
                    GameRegister.getGame(gameName).playerJoinQueue(p);
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")) {

                if(!GameRegister.isPlayerInGame(p)) {
                    p.sendMessage(MessagesConfig.get().getString("game.error-not-in-game"));
                    return true;
                }

                GameRegister.getPlayersGame(p).playerRemove(p);

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
                            p.sendMessage(MessagesConfig.get().getString("managing.error-game-running"));
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
                    return true;
                }
            }
            sender.sendMessage(MessagesConfig.get().getString("general.subcommand-not-found"));

        }
        else {
            sender.sendMessage(MessagesConfig.get().getString("general.error-send-from-console"));
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
