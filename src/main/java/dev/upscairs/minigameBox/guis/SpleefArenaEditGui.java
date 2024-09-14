package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.arenas.SpleefArena;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import dev.upscairs.minigameBox.arenas.creation_and_storing.PendingArenaEdits;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class SpleefArenaEditGui extends ArenaEditGui {

    public SpleefArenaEditGui(String[] args) {
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

        setInventory(currentInventory);
    }

    private ItemStack generateLayerItem() {
        ItemStack stack = new ItemStack(Material.LADDER, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(getDefaultHeaderComponent("Layers", "#C91F58"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(getArena().getLayerCount() + " Layers").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateSpleefBlockItem() {
        ItemStack stack = new ItemStack(getArena().getSpleefMaterial(), 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(getDefaultHeaderComponent("Spleef Block", "#8D989D"));

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
            default: return super.handleInvClick(clickedSlot);
        }
    }

    @Override
    public SpleefArena getArena() {
        return (SpleefArena) super.getArena();
    }

    @Override
    public void setArena(String name) {
        setArena((SpleefArena) GameRegister.getGame(name).getArena());
    }

}
