package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.config.SettingsFile;
import dev.upscairs.minigameBox.superclasses.guis.InteractableGui;
import dev.upscairs.minigameBox.superclasses.guis.ScrollableGui;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class ArenaListGui extends ScrollableGui implements InventoryHolder {

    private List<MinigameArena> arenas;
    private String clickedMode = "";

    public ArenaListGui(String[] args, List<MinigameArena> arenas) {
        super(args, new ArrayList<>(arenas));
        this.arenas = arenas;

        clickedMode = SettingsFile.get().getString("listClickAction");

        setupInventory();
    }

    @Override
    public void setupInventory() {
        setInventory(Bukkit.createInventory(this, 54, "Arena List (Page " + (getPage()+1) + "/" + (getMaxPageNumber()+1) + ")"));
        placeScrollItems();
        placeArenas();
    }

    public void placeArenas() {
        Inventory inv = super.getInventory();

        //Fetches arenas from list for requested page, as long as page is or arena list has items
        for(int i = getPage()*45; i < (getPage()+1)*45 && i < arenas.size(); i++) {
            inv.setItem((i%45), InvGuiUtils.generateArenaItem(clickedMode, arenas.get(i)));
        }
        setInventory(inv);
    }

    //Handle click on arena item
    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        if(clickedSlot < 45) {

            //No arena in slot
            if(clickedSlot+getPage()*45 >= arenas.size() ) {
                return this;
            }

            MinigameArena correspondingArena = arenas.get(clickedSlot+getPage()*45);
            Player guiViewer = getViewingPlayer();

            if(clickedMode.equalsIgnoreCase("tp")) {
                guiViewer.teleport(correspondingArena.getOutsideLocation());
                return null;
            }
            else if(clickedMode.equalsIgnoreCase("queue")) {
                GameRegister.getGame(correspondingArena.getName()).playerJoinQueue(guiViewer);

                return null;
            }
            return this;
        }
        return super.handleInvClick(clickedSlot);

    }
}
