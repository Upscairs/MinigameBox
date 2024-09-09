package dev.upscairs.minigameBox;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinigameCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            if(args.length > 2) {

                if(args[0].equalsIgnoreCase("join")) {
                    String arenaName = args[1];
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinEvent(p, arenaName));
                }


            }

        }

        return true;

    }

}
