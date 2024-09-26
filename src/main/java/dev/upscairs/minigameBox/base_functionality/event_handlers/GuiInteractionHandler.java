package dev.upscairs.minigameBox.base_functionality.event_handlers;

import dev.upscairs.minigameBox.superclasses.guis.InteractableGui;
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

                //If returned the same gui, it gets updated
                InteractableGui gui = ((InteractableGui) event.getClickedInventory().getHolder()).handleInvClick(event.getSlot());

                //Null closes inventory
                if(gui == null) {
                    event.getWhoClicked().closeInventory();
                }
                else {
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
