package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MinigameTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if(command.getName().equalsIgnoreCase("minigame")) {
            if(args.length == 1) {
                completions.addAll(getNonOpSubcoms());
                if(commandSender.hasPermission("minigamebox.manage")) {
                    completions.addAll(getOpSubcoms());
                }
            }
            else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("edit")
                || args[0].equalsIgnoreCase("delete")
                || args[0].equalsIgnoreCase("refresh")
                || args[0].equalsIgnoreCase("start")
                || args[0].equalsIgnoreCase("stop")
                || args[0].equalsIgnoreCase("join")
                || args[0].equalsIgnoreCase("queue")
                || args[0].equalsIgnoreCase("flush")) {
                    completions.addAll(getVisibleGames(commandSender));
                }
                else if(args[0].equalsIgnoreCase("create")) {
                    completions.addAll(getGameTypes());
                }
                else if(args[0].equalsIgnoreCase("dequeue")) {
                    GameRegister.getQueuedIngamePlayers().stream()
                            .filter(player -> GameRegister.getPlayersGame(player).getArena().getQueuedPlayers().contains(player))
                            .forEach(player -> completions.add(player.getName()));
                }
            }
        }

        return completions;

    }

    public ArrayList<String> getNonOpSubcoms() {
        ArrayList<String> keywords = new ArrayList<>();

        keywords.add("join");
        keywords.add("leave");
        keywords.add("list");
        keywords.add("queue");

        return keywords;
    }

    public ArrayList<String> getOpSubcoms() {
        ArrayList<String> keywords = new ArrayList<>();

        keywords.add("create");
        keywords.add("setpos");
        keywords.add("setup-cancel");
        keywords.add("edit");
        keywords.add("edit-input");
        keywords.add("edit-cancel");
        keywords.add("delete");
        keywords.add("refresh");
        keywords.add("stop");
        keywords.add("start");
        keywords.add("dequeue");
        keywords.add("flush");

        return keywords;
    }

    public ArrayList<String> getVisibleGames(CommandSender sender) {
        ArrayList<String> keywords = new ArrayList<>();

        GameRegister.getGames().keySet().stream()
                .filter(key -> sender.hasPermission("minigamebox.manage") || GameRegister.getGame(key).getArena().isVisible())
                .forEach(keywords::add);

        return keywords;

    }

    public ArrayList<String> getGameTypes() {
        ArrayList<String> keywords = new ArrayList<>();
        for(GameTypes gameType : GameTypes.values()) {
            keywords.add(gameType.getName());
        }
        return keywords;
    }






}
