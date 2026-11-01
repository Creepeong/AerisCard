package com.aeriscard.client;

import com.aeriscard.AerisCardMod;
import com.aeriscard.item.AerisCardItem;
import com.aeriscard.network.AerisCardAction;
import com.aeriscard.network.AerisCardActionPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;

public final class AerisCardInput {
    private static KeyMapping inspectKey;
    private static boolean wasHolding = false;

    public static void register() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(AerisCardMod.MOD_ID, "main"));
        inspectKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.aeriscard.inspect", InputConstants.Type.KEYSYM, InputConstants.KEY_Y, category));

        ClientTickEvents.START_CLIENT_TICK.register(AerisCardInput::onStartTick);
        ClientTickEvents.END_CLIENT_TICK.register(AerisCardInput::onEndTick);
    }

    /** 바닐라가 클릭을 처리하기 전에 좌클릭을 가로채서 "공격/채굴" 대신 left_click 애니메이션만 보냅니다. */
    private static void onStartTick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || !AerisCardItem.isHolding(player)) return;   // 카드를 안 들었으면 클릭을 건드리지 않음
        if (mc.gui.screen() != null) return;                               // 인벤토리 등 창이 열려 있으면 무시

        while (mc.options.keyAttack.consumeClick()) {
            AerisCardClientState.rerollWithinCurrentPool();
            send(AerisCardAction.LEFT_CLICK);
        }
    }

    private static void onEndTick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null) { wasHolding = false; return; }

        boolean holding = AerisCardItem.isHolding(player);
        if (holding && !wasHolding) {          // 손에 든 순간 = swap
            AerisCardClientState.rollRandomTexture();
            send(AerisCardAction.SWAP);
        }
        wasHolding = holding;

        while (inspectKey.consumeClick()) {    // Y 키: 텍스처는 유지, inspect 만
            if (holding && mc.gui.screen() == null) send(AerisCardAction.INSPECT);        }
    }

    private static void send(AerisCardAction action) {
        ClientPlayNetworking.send(new AerisCardActionPayload(action.id()));
    }

    private AerisCardInput() {}
}