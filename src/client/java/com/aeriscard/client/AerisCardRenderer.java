package com.aeriscard.client;

import com.aeriscard.item.AerisCardItem;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;

public class AerisCardRenderer
        extends GeoItemRenderer<AerisCardItem> {

    @Override
    public @Nullable RenderType getRenderType(
            GeoRenderState renderState,
            Identifier texture
    ) {
        return RenderTypes.itemTranslucent(texture);
    }

    public AerisCardRenderer() {
        super(new AerisCardModel());

        /*
         * =========================================================
         * WIDE / STEVE
         * =========================================================
         * 부모 본(RightArm_SwapAni 등)에는 큐브가 없으므로
         * 실제 큐브를 가진 자식 본(arm/sleeve)에 직접 등록한다.
         */

        // RightArm_SwapAni 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_arm4", PlayerModelType.WIDE));
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_sleeve4", PlayerModelType.WIDE));

        // RightArm4 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_arm5", PlayerModelType.WIDE));
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_sleeve5", PlayerModelType.WIDE));

        // LeftArm1_SwapAni 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_arm4", PlayerModelType.WIDE));
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_sleeve4", PlayerModelType.WIDE));

        // LeftArm4 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_arm5", PlayerModelType.WIDE));
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_sleeve5", PlayerModelType.WIDE));

        /*
         * =========================================================
         * SLIM / ALEX
         * =========================================================
         */

        // RightArm_SwapAni_slim 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_arm2", PlayerModelType.SLIM));
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_sleeve2", PlayerModelType.SLIM));

        // RightArm4_slim 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_arm3", PlayerModelType.SLIM));
        withRenderLayer(new PlayerSkinBoneLayer(this, "right_sleeve3", PlayerModelType.SLIM));

        // LeftArm1_SwapAni_slim 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_arm2", PlayerModelType.SLIM));
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_sleeve2", PlayerModelType.SLIM));

        // LeftArm4_slim 그룹
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_arm3", PlayerModelType.SLIM));
        withRenderLayer(new PlayerSkinBoneLayer(this, "left_sleeve3", PlayerModelType.SLIM));

        withRenderLayer(new CardGlowBoneLayer(this, "RightCard_SwapAni"));
    }

    @Override
    public void adjustRenderPose(
            RenderPassInfo<GeoRenderState> renderPassInfo
    ) {
        PoseStack poseStack =
                renderPassInfo.poseStack();

        GeoRenderState renderState =
                renderPassInfo.renderState();

        ItemDisplayContext context =
                renderState.getGeckolibData(
                        DataTickets.ITEM_RENDER_PERSPECTIVE
                );

        if (context == null) {
            return;
        }

        switch (context) {

            /*
             * =====================================================
             * 1인칭 오른손
             * =====================================================
             */
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(
                        0 / 32.0,
                        32 / 32.0,
                        -10 / 32.0
                );

                poseStack.mulPose(
                        Axis.XP.rotationDegrees(26.07f)
                );

                poseStack.mulPose(
                        Axis.YP.rotationDegrees(25.4f)
                );

                poseStack.mulPose(
                        Axis.ZP.rotationDegrees(2.88f)
                );
            }

            /*
             * =====================================================
             * 안 보이게 처리
             * =====================================================
             */
            case THIRD_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND,
                 GROUND,
                 HEAD,
                 FIXED,
                 ON_SHELF,
                 FIRST_PERSON_LEFT_HAND -> {
                poseStack.scale(
                        0.0f,
                        0.0f,
                        0.0f
                );
            }

            default -> {
            }
        }
    }

}