package de.ggmfrankie.ggmpipes.net;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;

public abstract class PipeNetwork<T extends PipeEntity> {
    public abstract void addAllNodes(T  entity);
    public abstract void removeAllNodes(T entity);
}
