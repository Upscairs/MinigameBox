package dev.upscairs.minigameBox.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.superclasses.MiniGame;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class InvGuiUtils {

    public static TextComponent generateDefaultTextComponent(String text, String colorHex) {

        return Component.text()
                .content(text)
                .color(TextColor.fromHexString(colorHex))
                .decoration(TextDecoration.ITALIC, false)
                .build();
    }

    public static TextComponent generateDefaultHeaderComponent(String text, String colorHex) {
        return Component.text()
                .content(text)
                .color(TextColor.fromHexString(colorHex))
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
                .build();
    }

    public static ItemStack generateCustomUrlHeadStack(String url) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(url));
        } catch (MalformedURLException e) {
            Bukkit.getLogger().warning("Head Database seems to be down");
            return stack;
        }
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack generateArenaItem(String clickedMode, MinigameArena arena) {
        ItemStack stack = new ItemStack(arena.getRepresentingItem(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent(arena.getName(), "#00AA00"));

        MiniGame game = GameRegister.getGame(arena.getName());

        List<Component> lore = new ArrayList<>();

        lore.add(InvGuiUtils.generateDefaultTextComponent(GameTypes.getFromArenaClass(arena.getClass()).getName(), "#55FF55"));

        lore.add(InvGuiUtils.generateDefaultTextComponent(arena.getDescription(), "#AAAAAA").decoration(TextDecoration.ITALIC, true));

        lore.add(InvGuiUtils.generateDefaultTextComponent("", "#000000"));

        if(game.isGameRunning()) {
            lore.add(InvGuiUtils.generateDefaultTextComponent("Game running...", "#55FF55").decoration(TextDecoration.BOLD, true));
        }
        else if(!game.isGameRunning() && arena.isContinuous()) {
            lore.add(InvGuiUtils.generateDefaultTextComponent("Waiting for players...", "#FF5555").decoration(TextDecoration.BOLD, true));
        }
        else {
            lore.add(InvGuiUtils.generateDefaultTextComponent("No game running.", "#AAAAAA").decoration(TextDecoration.BOLD, true));
        }

        lore.add(InvGuiUtils.generateDefaultTextComponent("Players in queue: ", "#FFAA00").append(InvGuiUtils.generateDefaultTextComponent(arena.getQueueLength() + "", "#FF5555")));

        if(clickedMode.equalsIgnoreCase("tp")) {
            lore.add(InvGuiUtils.generateDefaultTextComponent("Click to teleport!", "#55FF55").decoration(TextDecoration.ITALIC, true));
        }
        else if(clickedMode.equalsIgnoreCase("queue")) {
            lore.add(InvGuiUtils.generateDefaultTextComponent("Click to join queue!", "#55FF55").decoration(TextDecoration.ITALIC, true));
        }

        meta.lore(lore);

        stack.setItemMeta(meta);
        return stack;
    }

}
