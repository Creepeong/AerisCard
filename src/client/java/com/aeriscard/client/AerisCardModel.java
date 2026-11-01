package com.aeriscard.client;

import com.aeriscard.AerisCardMod;
import com.aeriscard.item.AerisCardItem;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

public class AerisCardModel extends GeoModel<AerisCardItem> {

    private static final Identifier MODEL =
            Identifier.fromNamespaceAndPath(
                    AerisCardMod.MOD_ID,
                    "item/aeriscard"
            );

    private static final Identifier ANIMATIONS =
            Identifier.fromNamespaceAndPath(
                    AerisCardMod.MOD_ID,
                    "item/aeriscard"
            );

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getAnimationResource(
            AerisCardItem animatable
    ) {
        return ANIMATIONS;
    }

    @Override
    public Identifier getTextureResource(
            GeoRenderState renderState
    ) {
        /*
         * 카드 본체 텍스처.
         *
         * 플레이어 스킨과는 전혀 관계 없음.
         */
        return AerisCardClientState.currentTexture();
    }
}