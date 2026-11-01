package com.aeriscard.client;

import com.aeriscard.item.AerisCardItem;
import com.geckolib.cache.model.cuboid.CuboidGeoBone;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.CustomBoneTextureGeoLayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class CardGlowBoneLayer extends CustomBoneTextureGeoLayer<
        AerisCardItem, GeoItemRenderer.RenderData, GeoRenderState> {

    public CardGlowBoneLayer(GeoItemRenderer<AerisCardItem> renderer, String boneName) {
        super(renderer, boneName, AerisCardClientState.currentTexture());
    }

    @Override
    public boolean shouldRenderBone(GeoRenderState renderState) {
        return true;
    }

    @Override
    protected Identifier getTextureResource(GeoRenderState renderState) {
        return AerisCardClientState.currentTexture();
    }

    @Override
    protected RenderType getRenderType(GeoRenderState renderState, Identifier texture) {
        return RenderTypes.itemTranslucent(texture);
    }

    @Override
    public void preRender(RenderPassInfo<GeoRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (!renderPassInfo.willRender()) {
            return;
        }

        renderPassInfo.addBoneUpdater((_, snapshots) -> snapshots.get(this.boneName)
                .filter(snapshot -> snapshot.getBone() instanceof CuboidGeoBone)
                .ifPresent(snapshot -> {
                    boolean skipChildren = snapshot.areChildrenHidden();
                    snapshot.skipRender(true);
                    snapshot.skipChildrenRender(skipChildren);
                }));
    }
}