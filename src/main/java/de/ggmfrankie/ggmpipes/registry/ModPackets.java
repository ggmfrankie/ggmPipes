package de.ggmfrankie.ggmpipes.registry;

import de.ggmfrankie.ggmpipes.networking.SetConnectionsPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class ModPackets {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SetConnectionsPacket.TYPE,
                SetConnectionsPacket.STREAM_CODEC,
                ServerPayloadHandler::handleDataOnMain
        )
    }
}
