package dev.upscairs.minigameBox.utils;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
import dev.upscairs.minigameBox.base_functionality.managing.config.SettingsFile;
import dev.upscairs.minigameBox.superclasses.MiniGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GameUtils {

    public static void broadcastMessage(Location center, String message) {

        if (!SettingsFile.get().getBoolean("arena-broadcast")) {
            return;
        }

        double range = SettingsFile.get().getDouble("broadcast-range");

        for (Player player : center.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= range) {
                player.sendMessage(message);
            }
        }


    }

    public static void grantWinnersReward(Set<Player> winners, Set<Player> losers, MiniGame game) {

        //Losers names seperated by comma
        StringBuilder losersString = new StringBuilder();

        for (Player loser : losers) {
            losersString.append(loser.getName() + ", ");
        }
        if (losersString.length() > 2) {
            losersString.delete(losersString.length() - 2, losersString.length());
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedTime = LocalDateTime.now().format(formatter);


        winners.forEach(winner -> {

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();

            String bookTitle = MessagesConfig.get().getString("rewards.book-title");
            String bookAuthor = MessagesConfig.get().getString("rewards.book-author");
            String bookText = MessagesConfig.get().getString("rewards.book-text");

            /*
            %a - Arena Name
            %m - Gamemode
            %p - Rewarded player
            %g - Game Name
            %l - Loser List
            %t - Time
            %n - Line Break
             */
            Map<Character, String> replacements = new HashMap<>();
            replacements.put('a', game.getArena().getName());
            replacements.put('m', GameTypes.getFromGameClass(game.getClass()).getName());
            replacements.put('p', winner.getName());
            replacements.put('g', game.getArena().getName());
            replacements.put('l', losersString.toString());
            replacements.put('t', formattedTime);
            replacements.put('n', "\n");

            System.out.println("Hallo: "+interpolateString(bookTitle, replacements));
            System.out.println(interpolateString(bookAuthor, replacements));
            System.out.println(interpolateString(bookText, replacements));

            bookMeta.setTitle(interpolateString(bookTitle, replacements));
            bookMeta.setAuthor(interpolateString(bookAuthor, replacements));
            bookMeta.addPage(interpolateString(bookText, replacements));



            book.setItemMeta(bookMeta);
            winner.getInventory().addItem(book);
        });

    }

    public static String interpolateString(String replaceString, Map<Character, String> replacements) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < replaceString.length(); i++) {
            if (replaceString.charAt(i) == '%' && i + 1 < replaceString.length()) {
                char key = replaceString.charAt(i + 1);
                if (replacements.containsKey(key)) {
                    result.append(replacements.get(key));
                    i++;
                }
                else {
                    result.append('%').append(key);
                    i++;
                }
            }
            else {
                result.append(replaceString.charAt(i));
            }
        }

        return result.toString();
    }

}
