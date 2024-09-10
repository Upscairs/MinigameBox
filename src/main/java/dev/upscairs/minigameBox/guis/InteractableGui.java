package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.MinigameBox;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class InteractableGui implements InventoryHolder {

    private Inventory inventory;
    private final MinigameBox plugin;
    private String[] args;


    public InteractableGui(String[] args) {

        this.plugin = (MinigameBox) Bukkit.getPluginManager().getPlugin("MinigameBox");
        this.args = args;

    }

    public void setupInventory() {
        inventory = Bukkit.createInventory((InventoryHolder) this, 27, "Gui Template");
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MinigameBox getPlugin() {
        return plugin;
    }

    public String getArg(int index) {
        return args[index];
    }

    public String[] getArgs() {
        return args;
    }

    public InteractableGui handleInvClick(int clickedSlot) {
        return null;
    }

}
