package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaListGui extends ScrollableGui implements InventoryHolder {

    private List<MinigameArena> arenas;

    public ArenaListGui(String[] args, List<MinigameArena> arenas) {
        super(args, new ArrayList<Object>(arenas));
        this.arenas = arenas;
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
        for(int i = getPage()*45; i < (getPage()+1)*45 && i < arenas.size(); i++) {
            inv.setItem((i%45), generateArenaItem(arenas.get(i)));
        }
    }

    private ItemStack generateArenaItem(MinigameArena arena) {
        ItemStack stack = new ItemStack(arena.getRepresentingItem(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.getDefaultHeaderComponent(arena.getName(), "#29B6F6"));

        //TODO
        //List<String> lore = new ArrayList<>();
        //meta.setLore(lore);

        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        if(clickedSlot < 45) {
            if(clickedSlot+getPage()*45 >= arenas.size() ) {
                return this;
            }
            MinigameArena correspondingArena = arenas.get(clickedSlot+getPage()*45);
            return null; //TODO
        }
        return super.handleInvClick(clickedSlot);

    }
}
