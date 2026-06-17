package de.ggmfrankie.ggmpipes.items.tileentity.filter;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NullMarked;

public class BasicItemFilter implements Filter<BasicItemFilter, ItemStack>, Container {
    private final ItemPipeEntity blockEntity;
    private final NonNullList<ItemStack> items;

    public BasicItemFilter(ItemPipeEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.items = NonNullList.withSize(
                12,
                ItemStack.EMPTY
        );
    }

    public BasicItemFilter(BasicItemFilter other) {
        this.blockEntity = other.blockEntity;
        this.items = NonNullList.copyOf(other.items);
    }

    public BasicItemFilter copy(){
        return new BasicItemFilter(this);
    }

    @Override
    public boolean isValid(ItemStack incoming) {
        for (var itemStack : items){
            if (ItemStack.isSameItem(incoming, itemStack)) return true;
        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    @NullMarked
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    @NullMarked
    public ItemStack removeItem(int slot, int amount) {
        ContainerHelper.removeItem(this.items, slot, amount);
        this.setChanged();
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public ItemStack removeItemNoUpdate(int slot) {
        ContainerHelper.takeItem(this.items, slot);
        this.setChanged();
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public void setItem(int slot, ItemStack stack) {
        stack.limitSize(1);
        this.items.set(slot, stack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        blockEntity.setChanged();
    }

    @Override
    @NullMarked
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
        this.setChanged();
    }
}
