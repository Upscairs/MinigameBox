package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.superclasses.guis.InteractableGui;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaDeleteConfirmGui extends InteractableGui {

    private MinigameArena arena;
    private InteractableGui parentGui;

    public ArenaDeleteConfirmGui(String[] args, InteractableGui parentGui) {
        super(args);

        setArena(args[1]);
        this.parentGui = parentGui;

        setupInventory();
    }

    @Override
    public void setupInventory() {
        Inventory currentInventory = Bukkit.createInventory((InventoryHolder) this, 27, ("Delete Arena?"));
        setInventory(currentInventory);

        placeItemsInGui();
    }

    public void placeItemsInGui() {
        Inventory inv = getInventory();

        inv.setItem(4, InvGuiUtils.generateArenaItem("none", arena));
        inv.setItem(10, generateDeleteItem());
        inv.setItem(16, generateCancelItem());

        setInventory(inv);
    }

    private ItemStack generateDeleteItem() {
        ItemStack stack = new ItemStack(Material.BARRIER);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent("Delete arena", "#FF5555"));

        stack.setItemMeta(meta);

        return stack;

    }

    private ItemStack generateCancelItem() {
        ItemStack stack = new ItemStack(Material.STRUCTURE_VOID);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent("Cancel", "#55FFFF"));

        stack.setItemMeta(meta);

        return stack;
    }

    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        switch (clickedSlot) {
            case 10: {
                getViewingPlayer().performCommand("minigame delete " + arena.getName());
                return null;
            }
            case 16: {
                return parentGui;
            }
            default: return super.handleInvClick(clickedSlot);

        }
    }



    public void setArena(String name) {
        setArena(GameRegister.getGame(name).getArena());
    }

    public void setArena(MinigameArena arena) {
        this.arena = arena;
    }

}
