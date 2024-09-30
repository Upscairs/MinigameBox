package dev.upscairs.minigameBox.superclasses.guis;

import dev.upscairs.minigameBox.utils.InvGuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ScrollableGui extends InteractableGui implements InventoryHolder {

    private int page;
    private int maxPageNumber;
    private List<Object> objects;
    private int contentSize;

    public ScrollableGui(String[] args, List<Object> objects) {
        super(args);
        try {
            page = Integer.parseInt(args[1]);
            if(page < 0) page = 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Page number must be an integer.");
        }
        this.objects = objects;
        this.contentSize = objects.size();
        maxPageNumber = (contentSize-1)/45;
    }

    @Override
    public void setupInventory() {

        Inventory inv = Bukkit.createInventory(this, 54, "Scrollable Gui");
        setInventory(inv);

        placeItemsInGui();

    }

    public void placeItemsInGui() {

        placeScrollItems();

    }

    public void placeScrollItems() {

        Inventory inv = getInventory();

        if(page > 0) {
            inv.setItem(45, generateScrollLeftItem());
        }
        if(page < maxPageNumber) {
            inv.setItem(53, generateScrollRightItem());
        }

        setInventory(inv);
    }

    public ItemStack generateScrollLeftItem() {
        ItemStack stack = InvGuiUtils.generateCustomUrlHeadStack("http://textures.minecraft.net/texture/e35e42fc7060c223acc965f7c5996f272644af40a4723a372f5903f8e9f188e7");
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent("Previous Page", "#B1B1B1"));
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack generateScrollRightItem() {
        ItemStack stack = InvGuiUtils.generateCustomUrlHeadStack("http://textures.minecraft.net/texture/aee0f82fb33f6cfa5169b9f5eafe4dc1c73618c9783b131adada411d8f605505");
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(InvGuiUtils.generateDefaultHeaderComponent("Next Page", "#B1B1B1"));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public InteractableGui handleInvClick(int clickedSlot) {
        String[] currentArgs = getArgs();
        switch(clickedSlot) {
            case 45: {
                if(page > 0) {
                    currentArgs[1] = String.valueOf(page - 1);
                    try {
                        return (this.getClass()).getDeclaredConstructor(String[].class, List.class).newInstance((Object) getArgs(), objects);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                return this;
            }
            case 53: {
                if (page < maxPageNumber) {
                    currentArgs[1] = String.valueOf(page + 1);
                    try {
                        return (this.getClass()).getDeclaredConstructor(String[].class, List.class).newInstance((Object) getArgs(), objects);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                return this;
            }
            default: return super.handleInvClick(clickedSlot);
        }
    }

    public int getPage() {
        return page;
    }

    public int getMaxPageNumber() {
        return maxPageNumber;
    }

}
