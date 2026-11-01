package com.aeriscard.client;

import com.aeriscard.item.AerisCardItem;
import com.aeriscard.item.ModItems;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.renderer.GeoItemRenderer;
import com.google.common.base.Suppliers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;

import java.util.function.Supplier;

public class AerisCardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModItems.AERIS_CARD.geoRenderProvider.setValue(new GeoRenderProvider() {
            private final Supplier<AerisCardRenderer> renderer = Suppliers.memoize(AerisCardRenderer::new);

            @Override
            public GeoItemRenderer<AerisCardItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
        AerisCardInput.register();

        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> {
            return AerisCardItem.isHolding(player);
        });
    }
}
