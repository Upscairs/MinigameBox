package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.MinigameBox;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ArenaEditGui extends InteractableGui {

    public ArenaEditGui(MinigameBox plugin, String[] args) {
        super(plugin, args);

        setupInventory();
    }

    @Override
    public void setupInventory() {

        Inventory currentInventory = Bukkit.createInventory((InventoryHolder) this, 36, ("Edit Profile"));

        //Set items

        setInventory(currentInventory);

    }


    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        switch (clickedSlot) {
            case 0:

        }
        return null;
    }

}
