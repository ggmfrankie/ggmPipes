package de.ggmfrankie.ggmpipes;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.network.ItemPipeNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

@EventBusSubscriber(value = Dist.DEDICATED_SERVER)
public class NetworkHandler {
    private static final Map<UUID, ItemPipeNetwork> itemPipeNetworks = new HashMap<>();

    public static UUID createNewItemNetwork(){
        UUID id = UUID.randomUUID();
        itemPipeNetworks.put(id, new ItemPipeNetwork());
        return id;
    }

    public static void addToNetwork(UUID id, ItemPipeEntity entity) {
        ItemPipeNetwork network = itemPipeNetworks.get(id);
        assert network != null;

        network.addAllNodes(entity);
    }

    public static void removeFromNetwork(UUID id, ItemPipeEntity entity) {
        ItemPipeNetwork network = itemPipeNetworks.get(id);
        assert network != null;

        network.removeAllNodes(entity);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (var network : itemPipeNetworks.values()) {
            network.update();
        }
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event) {
        //TODO
    }
}
