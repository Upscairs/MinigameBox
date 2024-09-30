package dev.upscairs.minigameBox.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
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

}
