package de.ggmfrankie.ggmpipes.items.tileentity.filter;

import net.minecraft.world.item.ItemStack;

public interface Filter <F extends Filter<F, T>, T> {
    boolean isValid(T obj);
}
