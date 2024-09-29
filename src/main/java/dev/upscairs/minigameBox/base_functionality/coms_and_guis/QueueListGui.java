package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.superclasses.guis.InteractableGui;
import dev.upscairs.minigameBox.superclasses.guis.ScrollableGui;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class QueueListGui extends ScrollableGui implements InventoryHolder {

    private List<Player> players;

    public QueueListGui(String[] args, List<Player> players) {
        super(args, new ArrayList<>(players));
        this.players = players;
        setupInventory();
    }

    public void setupInventory() {
        setInventory(Bukkit.createInventory(this, 54, "Queue (Page " + (getPage()+1) + "/" + (getMaxPageNumber()+1) + ")"));
        placeScrollItems();
        placePlayerHeads();
    }

    public void placePlayerHeads() {
        Inventory inv = super.getInventory();

        for(int i = getPage()*45; i < (getPage()+1)*45 && i < players.size(); i++) {
            inv.setItem((i%45), generatePlayerItem(players.get(i), i));
        }
        setInventory(inv);
    }

    private ItemStack generatePlayerItem(Player player, int position) {

        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) stack.getItemMeta();

        meta.setOwner(player.getName());

        TextComponent header = InvGuiUtils.getDefaultHeaderComponent("#" + (++position) + " ", "#D6AE35");
        header = header.append(InvGuiUtils.getDefaultHeaderComponent(player.getName(), "#2F98C6"));

        meta.displayName(header);

        stack.setItemMeta(meta);

        return stack;

    }

    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        return super.handleInvClick(clickedSlot);
    }



}
