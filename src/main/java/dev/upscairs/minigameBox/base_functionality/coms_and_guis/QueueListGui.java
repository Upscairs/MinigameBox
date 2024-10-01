package dev.upscairs.minigameBox.base_functionality.coms_and_guis;

import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.config.MessagesConfig;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueueListGui extends ScrollableGui implements InventoryHolder {

    private List<Player> players;
    private String gameName;

    public QueueListGui(String[] args, List<Player> players) {
        super(args, new ArrayList<>(players));
        this.players = players;
        this.gameName = args[2];
        setupInventory();
    }

    public void setupInventory() {
        setInventory(Bukkit.createInventory(this, 54, "Queue (Page " + (getPage()+1) + "/" + (getMaxPageNumber()+1) + ")"));
        placeScrollItems();
        placePlayerHeads();
        placeQueueItems();
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

        TextComponent header = InvGuiUtils.generateDefaultHeaderComponent("#" + (++position) + " ", "#D6AE35");
        header = header.append(InvGuiUtils.generateDefaultHeaderComponent(player.getName(), "#2F98C6"));

        meta.displayName(header);

        stack.setItemMeta(meta);

        return stack;

    }

    public void placeQueueItems() {
        Inventory inv = super.getInventory();

        inv.setItem(49, generateJoinLeaveItem());
        inv.setItem(50, generateFlushItem());

        setInventory(inv);
    }

    private ItemStack generateJoinLeaveItem() {
        boolean playerIngame = GameRegister.isPlayerInGame(getPlayer());
        ItemStack stack = new ItemStack(playerIngame ? Material.SOUL_CAMPFIRE : Material.CAMPFIRE);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent(playerIngame ? "Leave Game" : "Join Game", playerIngame ? "#FF5555" : "#55FF55"));

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateFlushItem() {
        ItemStack stack = new ItemStack(Material.BUCKET);

        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent("Flush Queue", "#F88C02"));

        stack.setItemMeta(meta);

        return stack;

    }


    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        switch (clickedSlot) {
            case 49: {
                if(GameRegister.isPlayerInGame(getPlayer())) {
                    getPlayer().performCommand("minigame leave");
                }
                else {
                    getPlayer().performCommand("minigame join " + gameName);
                }
                return new QueueListGui(getArgs(), GameRegister.getGame(gameName).getArena().getQueuedPlayers().stream().toList());
            }
            case 50: {
                if(getPlayer().hasPermission("minigamebox.manange")) {

                    getPlayer().performCommand("minigame flush " + gameName);

                    return new QueueListGui(getArgs(), GameRegister.getGame(gameName).getArena().getQueuedPlayers().stream().toList());
                }
            }
            default: {
                return super.handleInvClick(clickedSlot);
            }
        }

    }



}
