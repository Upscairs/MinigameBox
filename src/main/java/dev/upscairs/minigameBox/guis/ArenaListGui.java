package dev.upscairs.minigameBox.guis;

import dev.upscairs.minigameBox.arenas.MinigameArena;
import dev.upscairs.minigameBox.config.SettingsFile;
import dev.upscairs.minigameBox.events.custom.PlayerJoinQueueEvent;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaListGui extends ScrollableGui implements InventoryHolder {

    private List<MinigameArena> arenas;
    private String clickedMode = "";

    public ArenaListGui(String[] args, List<MinigameArena> arenas) {
        super(args, new ArrayList<Object>(arenas));
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
        for(int i = getPage()*45; i < (getPage()+1)*45 && i < arenas.size(); i++) {
            inv.setItem((i%45), generateArenaItem(arenas.get(i)));
        }
    }

    private ItemStack generateArenaItem(MinigameArena arena) {
        ItemStack stack = new ItemStack(arena.getRepresentingItem(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.getDefaultHeaderComponent(arena.getName(), "#29B6F6"));

        List<String> lore = new ArrayList<>();

        if(clickedMode.equalsIgnoreCase("tp")) {
            lore.add("Click to teleport!");
        }
        else if(clickedMode.equalsIgnoreCase("queue")) {
            lore.add("Click to join queue!");
        }

        meta.setLore(lore);

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

            Player p = Bukkit.getPlayer(UUID.fromString(getArg(0)));
            if(clickedMode.equalsIgnoreCase("tp")) {
                p.teleport(correspondingArena.getOutsideLocation());
                return null;
            }
            else if(clickedMode.equalsIgnoreCase("queue")) {
                Bukkit.getPluginManager().callEvent(new PlayerJoinQueueEvent(p, correspondingArena.getName()));
                return null;
            }
            return this;
        }
        return super.handleInvClick(clickedSlot);

    }
}
