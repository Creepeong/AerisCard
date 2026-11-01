package com.aeriscard.client.mixin;

import com.aeriscard.item.AerisCardItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow private ItemStack mainHandItem;
    @Shadow private ItemStack offHandItem;

    @ModifyVariable(method = "applyItemArmTransform", at = @At("HEAD"), argsOnly = true)
    private float aeriscard$forceFullEquip(float equipProgress, PoseStack poseStack, HumanoidArm arm) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return equipProgress;

        // "지금 실제로 화면에 그려지고 있는" 아이템 기준 (내려가는 중이면 아직 AerisCard)
        boolean isMainHandCall = arm == player.getMainArm();
        ItemStack renderedStack = isMainHandCall ? this.mainHandItem : this.offHandItem;

        if (renderedStack.getItem() instanceof AerisCardItem) {
            return 1.0f; // 올라올 때든 내려갈 때든, AerisCard가 그려지는 동안엔 항상 완전히 든 상태로 고정
        }
        return equipProgress;
    }
}