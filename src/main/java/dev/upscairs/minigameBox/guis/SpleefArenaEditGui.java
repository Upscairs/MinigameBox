package dev.upscairs.minigameBox.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SpleefArenaEditGui extends ArenaEditGui {

    public SpleefArenaEditGui(String[] args) {
        super(args);

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

        return stack;
    }

    private ItemStack generateSpleefBlockItem() {
        ItemStack stack = new ItemStack(Material.WHITE_WOOL, 1);

        return stack;
    }


    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        switch (clickedSlot) {
            case 0:

        }
        return null;
    }

}
