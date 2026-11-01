package com.aeriscard.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.SingletonGeoAnimatable;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.ClientUtil;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.function.Consumer;

public final class AerisCardItem extends Item implements GeoItem {
    public static final String CONTROLLER_NAME = "aeris_controller";

    // ★ 아래 문자열은 본인 .animation.json 안의 애니메이션 키와 정확히 같아야 합니다.
    //   (예: "animation.aeriscard.swap" 같은 형태라면 그대로 적으세요)
    private static final RawAnimation SWAP_ANIM = RawAnimation.begin().thenPlay("animation.aeriscard.swap");
    private static final RawAnimation LEFT_CLICK_ANIM = RawAnimation.begin().thenPlay("animation.aeriscard.left_click");
    private static final RawAnimation INSPECT_ANIM = RawAnimation.begin().thenPlay("animation.aeriscard.Inspect");

    // 소스 분리(client/main) 구조라서 렌더러를 직접 참조하지 못합니다. 클라이언트 초기화에서 채웁니다.
    public final MutableObject<Object> geoRenderProvider = new MutableObject<>();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AerisCardItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);   // 서버 트리거 애니메이션에 필수
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void createGeoRenderer(Consumer consumer) {
        consumer.accept(this.geoRenderProvider.getValue());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(CONTROLLER_NAME, 0, animTest -> PlayState.CONTINUE)
                .receiveTriggeredAnimations()
                .triggerableAnim("swap", SWAP_ANIM)
                .triggerableAnim("left_click", LEFT_CLICK_ANIM)
                .triggerableAnim("inspect", INSPECT_ANIM)
                .setSoundKeyframeHandler(state -> {
                    Player player = ClientUtil.getClientPlayer();
                    if (player != null) {
                        Identifier id = Identifier.parse(state.keyframeData().getSound());
                        player.playSound(SoundEvent.createVariableRangeEvent(id), 1.0f, 1.0f);
                    }
                }));
    }

    @Override
    public boolean isPerspectiveAware() { return true; }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }

    public static boolean isHolding(Player player) {
        return player.getMainHandItem().getItem() instanceof AerisCardItem;
    }
}