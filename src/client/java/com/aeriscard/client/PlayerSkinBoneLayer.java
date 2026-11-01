package com.aeriscard.client;

import com.aeriscard.item.AerisCardItem;
import com.geckolib.cache.model.cuboid.CuboidGeoBone;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.CustomBoneTextureGeoLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;

public class PlayerSkinBoneLayer extends CustomBoneTextureGeoLayer<
        AerisCardItem,
                GeoItemRenderer.RenderData,
GeoRenderState> {

private static final Identifier FALLBACK_TEXTURE =
        Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png");

private final PlayerModelType requiredModelType;

public PlayerSkinBoneLayer(
        GeoItemRenderer<AerisCardItem> renderer,
        String boneName,
        PlayerModelType requiredModelType
) {
    super(renderer, boneName, FALLBACK_TEXTURE);
    this.requiredModelType = requiredModelType;
}

private static @Nullable PlayerSkin getPlayerSkin() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null) {
        return null;
    }
    return minecraft.player.getSkin();
}
    @Override
    protected RenderType getRenderType(GeoRenderState renderState, Identifier texture) {
        return RenderTypes.itemCutout(texture);
    }

// 재렌더 여부만 결정 (스킨으로 다시 그릴지 말지)
@Override
public boolean shouldRenderBone(GeoRenderState renderState) {
    PlayerSkin skin = getPlayerSkin();
    return skin != null && skin.model() == requiredModelType;
}

@Override
protected Identifier getTextureResource(GeoRenderState renderState) {
    PlayerSkin skin = getPlayerSkin();
    return (skin == null) ? FALLBACK_TEXTURE : skin.body().texturePath();
}

// 숨기기는 shouldRenderBone과 무관하게 항상 실행 (핵심 수정)
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