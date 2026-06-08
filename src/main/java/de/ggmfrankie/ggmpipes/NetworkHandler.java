package de.ggmfrankie.ggmpipes;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.network.ItemPipeNetwork;

import java.util.*;

public class NetworkHandler {
    private static final Map<UUID, ItemPipeNetwork> itemPipeNetworks = new HashMap<>();

    public static UUID createNewItemNetwork(){
        UUID id = UUID.randomUUID();
        itemPipeNetworks.put(id, new ItemPipeNetwork());
        return id;
    }

    public static void tickAllNetworks(){
        for (var network : itemPipeNetworks.values()){
            network.update();
        }
    }

    public static void addToNetwork(UUID id, ItemPipeEntity entity){
        ItemPipeNetwork network = itemPipeNetworks.get(id);
        assert network != null;

        network.addAllNodes(entity);
    }
}
