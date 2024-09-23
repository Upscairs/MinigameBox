package dev.upscairs.minigameBox.superclasses.guis;

import dev.upscairs.minigameBox.superclasses.MinigameArena;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameRegister;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.changing.PendingArenaEdits;
import dev.upscairs.minigameBox.base_functionality.managing.arenas_and_games.storing.GameTypes;
import dev.upscairs.minigameBox.superclasses.MiniGame;
import dev.upscairs.minigameBox.utils.InvGuiUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

public class ArenaEditGui extends InteractableGui {

    private MinigameArena arena;
    private boolean gameRunning;

    public ArenaEditGui(String[] args) {
        super(args);

        setArena(args[1]);
        gameRunning = GameRegister.getGame(args[1]).isGameRunning();

        setupInventory();
    }

    @Override
    public void setupInventory() {

        Inventory currentInventory = Bukkit.createInventory((InventoryHolder) this, 54, ("Edit Arena"));
        setInventory(currentInventory);

        placeItemsInGui();

    }

    public void placeItemsInGui() {
        Inventory currentInventory = getInventory();

        currentInventory.setItem(11, generateMinPlayerItem());
        currentInventory.setItem(20, generateMaxPlayerItem());
        currentInventory.setItem(12, generateFillupItem());
        currentInventory.setItem(21, generatePreparetimeItem());
        currentInventory.setItem(13, generateQueueItem());
        currentInventory.setItem(22, generateContinousItem());
        currentInventory.setItem(14, generateVisibilityItem());
        currentInventory.setItem(23, generateRepresentingItem());
        currentInventory.setItem(15, generateDescriptionItem());
        currentInventory.setItem(53, generateRunningItem());

        setInventory(currentInventory);
    }


    private ItemStack generateMinPlayerItem() {
        ItemStack stack = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Min required Players", "#BCB8B8"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.getMinPlayers() + " Players required").build());
        meta.lore(lore);

        stack.setItemMeta(meta);


        return stack;
    }

    private ItemStack generateMaxPlayerItem() {
        ItemStack stack = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Max Players", "#F5C85A"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.getMaxPlayers() + " Players maximum").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateFillupItem() {
        ItemStack stack = new ItemStack(Material.CAULDRON, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Waiting time for more players", "#929292"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.getFillupWaitingTimeSec() + " Seconds").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generatePreparetimeItem() {
        ItemStack stack = new ItemStack(Material.CLOCK, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Setup time in arena", "#4FC3F7"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.getSetupTimeSec() + " Seconds").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateQueueItem() {
        ItemStack stack = new ItemStack(Material.CHAIN, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Queue Open", "#70828B"));
        meta.setEnchantmentGlintOverride(arena.isQueueOpen());

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.isQueueOpen() ? "Yes" : "No").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateContinousItem() {
        ItemStack stack = new ItemStack(Material.REPEATER, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Auto-start next game", "#DE771C"));
        meta.setEnchantmentGlintOverride(arena.isContinuous());

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.isContinuous() ? "Enabled" : "Disabled").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateVisibilityItem() {
        ItemStack stack = new ItemStack(Material.ENDER_EYE, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Visible in list", "#992CAB"));
        meta.setEnchantmentGlintOverride(arena.isVisible());

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.isVisible() ? "Yes" : "No").build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateRepresentingItem() {
        ItemStack stack = new ItemStack(arena.getRepresentingItem(), 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Representing item in list", "#67B16B"));

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateDescriptionItem() {
        ItemStack stack = new ItemStack(Material.NAME_TAG, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Description", "#008B99"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text().content(arena.getDescription()).build());
        meta.lore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack generateRunningItem() {
        ItemStack stack = null;
        if(GameRegister.getGame(arena.getName()).isGameRunning()) {
            stack = new ItemStack(Material.GREEN_WOOL, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(InvGuiUtils.getDefaultHeaderComponent("Game running...", "#00DD00"));
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text().content("Click to force stop!").build());
            meta.lore(lore);
            stack.setItemMeta(meta);
        }
        else {
            stack = new ItemStack(Material.RED_WOOL, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(InvGuiUtils.getDefaultHeaderComponent("No game running", "#DD0000"));
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text().content("Click to force start! (ignores player numbers)").build());
            meta.lore(lore);
            stack.setItemMeta(meta);
        }

        return stack;
    }



    @Override
    public InteractableGui handleInvClick(int clickedSlot) {

        switch (clickedSlot) {
            case 11: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 0); return null;
            case 20: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 1); return null;
            case 12: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 2); return null;
            case 21: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 3); return null;
            case 13: arena.editArgs(5, arena.isQueueOpen() ? "false" : "true"); return getNewGui();
            case 22: arena.editArgs(4, arena.isContinuous() ? "false" : "true"); return getNewGui();
            case 14: arena.editArgs(6, arena.isVisible() ? "false" : "true"); return getNewGui();
            case 23: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 7); return null;
            case 15: PendingArenaEdits.newEditInstance(Bukkit.getPlayer(UUID.fromString(getArg(0))), getArena(), 8); return null;
            case 53: {
                MiniGame game = GameRegister.getGame(arena.getName());
                if(game.isGameRunning()) {
                    game.endGame(true);
                }
                else {
                    game.startGameFinal();
                }
                return getNewGui();
            }
            default: return super.handleInvClick(clickedSlot);
        }
    }

    private ArenaEditGui getNewGui() {
        ArenaEditGui gui = null;
        GameTypes gameType = GameTypes.getFromArenaClass(arena.getClass());
        try {
            gui = gameType.getEditGuiClass().getDeclaredConstructor(String[].class).newInstance((Object) super.getArgs());
        } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return gui;
    }

    public MinigameArena getArena() {
        return arena;
    }

    public void setArena(String name) {
        setArena(GameRegister.getGame(name).getArena());
    }

    public void setArena(MinigameArena arena) {
        this.arena = arena;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

}
