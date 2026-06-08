package de.ggmfrankie.ggmpipes.items.tileentity.network;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;

public abstract class PipeNetwork {
    public abstract void addAllNodes(PipeEntity entity);
    public abstract void removeAllNodes(PipeEntity entity);
}
