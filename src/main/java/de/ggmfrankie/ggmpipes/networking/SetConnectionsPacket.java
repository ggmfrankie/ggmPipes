package de.ggmfrankie.ggmpipes.networking;

import com.mojang.datafixers.types.Type;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import static de.ggmfrankie.ggmpipes.ggmPipes.MODID;

public record SetConnectionsPacket (
        BlockPos pos,
        Direction dir,
        boolean isInsert,
        boolean value
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetConnectionsPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "set_connections_packet"));

    public static final StreamCodec<ByteBuf, SetConnectionsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BlockPos.CODEC),
            SetConnectionsPacket::pos,
            ByteBufCodecs.fromCodec(Direction.CODEC),
            SetConnectionsPacket::dir,
            ByteBufCodecs.BOOL,
            SetConnectionsPacket::isInsert,
            ByteBufCodecs.BOOL,
            SetConnectionsPacket::value,
            SetConnectionsPacket::new
    );

    @Override
    @NullMarked
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
