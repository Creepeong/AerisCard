package com.aeriscard.network;

import com.aeriscard.AerisCardMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record AerisCardActionPayload(int actionId) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(AerisCardMod.MOD_ID, "action");
    public static final CustomPacketPayload.Type<AerisCardActionPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, AerisCardActionPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, AerisCardActionPayload::actionId, AerisCardActionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}