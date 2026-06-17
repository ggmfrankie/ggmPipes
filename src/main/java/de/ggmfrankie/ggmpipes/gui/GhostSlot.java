package de.ggmfrankie.ggmpipes.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

public class GhostSlot extends Slot {
    public GhostSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    @NullMarked
    public boolean mayPlace(ItemStack itemStack) {
        return true;
    }

    @Override
    @NullMarked
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    @NullMarked
    public ItemStack remove(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) {
        return Optional.empty();
    }
}
