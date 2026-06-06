package de.ggmfrankie.ggmpipes.items.tileentity.network;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;

public abstract class PipeNetwork {
    public abstract void addNode(PipeEntity entity);
    public abstract void removeNode(PipeEntity entity);
}
