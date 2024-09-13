package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.MinigameBox;
import dev.upscairs.minigameBox.arenas.creation_and_storing.GameRegister;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ArenaEditGui extends InteractableGui {

    private String[] arenaArgs;

    public ArenaEditGui(String[] args) {
        super(args);

        arenaArgs = GameRegister.getGame(args[1]).getArena().getRawArgs();

        setupInventory();
    }

    @Override
    public void setupInventory() {

        Inventory currentInventory = Bukkit.createInventory((InventoryHolder) this, 54, ("Edit Arena"));

        placeItemsInGui();

        setInventory(currentInventory);

    }

    public void placeItemsInGui() {
        Inventory currentInventory = getInventory();

        currentInventory.setItem(11, generateMinPlayerItem());
        currentInventory.setItem(20, generateMaxPlayerItem());
        currentInventory.setItem(12, generateFillupItem());
        currentInventory.setItem(21, generatePreparetimeItem());
        currentInventory.setItem(13, generateQueueItem());
        currentInventory.setItem(22, generateContinousItem());
        currentInventory.setItem(14, generateVisibilityItem());
        currentInventory.setItem(23, generateRepresentingItem());
        currentInventory.setItem(15, generateDescriptionItem());

        setInventory(currentInventory);
    }

    private ItemStack generateMinPlayerItem() {
        ItemStack stack = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 1);

        return stack;
    }

    private ItemStack generateMaxPlayerItem() {
        ItemStack stack = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1);

        return stack;
    }

    private ItemStack generateFillupItem() {
        ItemStack stack = new ItemStack(Material.CAULDRON, 1);

        return stack;
    }

    private ItemStack generatePreparetimeItem() {
        ItemStack stack = new ItemStack(Material.CLOCK, 1);

        return stack;
    }

    private ItemStack generateQueueItem() {
        ItemStack stack = new ItemStack(Material.CHAIN, 1);

        return stack;
    }

    private ItemStack generateContinousItem() {
        ItemStack stack = new ItemStack(Material.REPEATER, 1);

        return stack;
    }

    private ItemStack generateVisibilityItem() {
        ItemStack stack = new ItemStack(Material.ENDER_EYE, 1);

        return stack;
    }

    private ItemStack generateRepresentingItem() {
        ItemStack stack = new ItemStack(Material.SHEARS, 1);

        return stack;
    }

    private ItemStack generateDescriptionItem() {
        ItemStack stack = new ItemStack(Material.NAME_TAG, 1);

        return stack;
    }

    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        switch (clickedSlot) {
            case 0:

        }
        return null;
    }

    public String[] getArenaArgs() {
        return arenaArgs;
    }

}
