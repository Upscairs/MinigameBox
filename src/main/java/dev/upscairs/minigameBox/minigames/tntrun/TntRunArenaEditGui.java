package dev.upscairs.minigameBox.minigames.tntrun;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing.PendingArenaEdits;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.minigames.spleef.SpleefArena;
import dev.upscairs.minigameBox.superclasses.guis.ArenaEditGui;
import dev.upscairs.minigameBox.superclasses.guis.InteractableGui;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class TntRunArenaEditGui extends ArenaEditGui {

    public TntRunArenaEditGui(String[] args) {
        super(args);

        setArena(args[1]);

        setupInventory();
    }

    @Override
    public void setupInventory() {

        Inventory currentInventory = Bukkit.createInventory((InventoryHolder) this, 54, ("Edit Arena"));

        setInventory(currentInventory);


        super.placeItemsInGui();

        placeItemsInGui();

    }

    @Override
    public void placeItemsInGui() {
        Inventory currentInventory = getInventory();

        currentInventory.setItem(38, generateLayerItem());
        currentInventory.setItem(39, generateSpleefBlockItem());
        currentInventory.setItem(40, generateBlockBreakDelayItem());

        setInventory(currentInventory);
    }

    private ItemStack generateLayerItem() {
        ItemStack stack = new ItemStack(Material.LADDER, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Layers", "#C91F58"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(getArena().getLayerCount() + " Layers").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateSpleefBlockItem() {
        ItemStack stack = new ItemStack(getArena().getLayerMaterial(), 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Spleef Block", "#8D989D"));

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateBlockBreakDelayItem() {
        ItemStack stack = new ItemStack(Material.STONE_PRESSURE_PLATE);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Block Break Delay Ticks", "#E7BA33"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(getArena().getBreakDelayTicks() + " Ticks").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }


    @Override
    public InteractableGui handleInvClick(int clickedSlot) {

        switch (clickedSlot) {
            case 38: {
                PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 9);

                return null;
            }
            case 39: {
                PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 10);

                return null;
            }
            case 40: {
                PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 11);

                return null;
            }
            default: return super.handleInvClick(clickedSlot);
        }
    }

    @Override
    public TntRunArena getArena() {
        return (TntRunArena) super.getArena();
    }

    @Override
    public void setArena(String name) {
        setArena((TntRunArena) GameRegister.getGame(name).getArena());
    }

}
