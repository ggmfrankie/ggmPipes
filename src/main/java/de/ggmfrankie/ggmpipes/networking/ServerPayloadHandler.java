package de.ggmfrankie.ggmpipes.networking;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleDataOnMain(final SetConnectionsPacket data, final IPayloadContext context) {
        if (context.player().level().getBlockEntity(data.pos()) instanceof PipeEntity entity){
            Direction dir = data.dir();
            boolean val = data.value();

            if (data.isInsert())
                entity.setInsert(dir, val);
            else
                entity.setExtract(dir, val);
        }

    }
}
