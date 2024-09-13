package dev.upscairs.minigameBox.events.handling;

import dev.upscairs.minigameBox.guis.InteractableGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GuiInteractionHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        //Prevents errors on console
        if (event.getClickedInventory() == null) {
            return;
        }

        //Cancel interactions in guis
        if (event.getView().getTopInventory().getHolder() instanceof InteractableGui) {

            event.setCancelled(true);

            //Open sub-gui if possible
            if (event.getClickedInventory().getHolder() instanceof InteractableGui) {

                InteractableGui gui = ((InteractableGui) event.getClickedInventory().getHolder()).handleInvClick(event.getSlot());

                if (gui != null) {
                    event.getWhoClicked().openInventory(gui.getInventory());
                }
            }

        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        //Cancel interactions in guis
        if (event.getView().getTopInventory() instanceof InteractableGui) {
            event.setCancelled(true);
        }
    }

}
