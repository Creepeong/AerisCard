package com.aeriscard;

import com.aeriscard.item.AerisCardItem;
import com.aeriscard.item.ModItems;
import com.aeriscard.network.AerisCardAction;
import com.aeriscard.network.AerisCardActionPayload;
import com.geckolib.animatable.GeoItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class AerisCardMod implements ModInitializer {
    public static final String MOD_ID = "aeriscard";

    @Override
    public void onInitialize() {
        ModItems.initialize();

        // 클라이언트 -> 서버 패킷 등록 (양쪽에서 실행되는 공통 초기화에서)
        PayloadTypeRegistry.serverboundPlay().register(AerisCardActionPayload.TYPE, AerisCardActionPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(AerisCardActionPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof AerisCardItem card)) return;   // 서버 검증
            AerisCardAction action = AerisCardAction.byId(payload.actionId());
            if (action == null) return;
            ServerLevel level = (ServerLevel) player.level();
            card.triggerAnim(player, GeoItem.getOrAssignId(stack, level),
                    AerisCardItem.CONTROLLER_NAME, action.triggerName());
        });

        // 매 서버 틱마다 오프핸드(왼손)를 확인해서, 카드가 거기 있으면 바로 인벤토리로 돌려보냅니다.
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
                if (offhand.getItem() instanceof AerisCardItem) {
                    if (player.getInventory().add(offhand)) {
                        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                    // 인벤토리가 꽉 차서 못 넣으면 이번 틱은 그냥 두고, 다음 틱에 다시 시도합니다
                }
            }
        });
    }
}