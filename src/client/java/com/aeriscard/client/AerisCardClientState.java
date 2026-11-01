package com.aeriscard.client;

import com.aeriscard.AerisCardMod;
import net.minecraft.resources.Identifier;

import java.util.concurrent.ThreadLocalRandom;

public final class AerisCardClientState {

    private static Identifier tex(String name) {
        return Identifier.fromNamespaceAndPath(
                AerisCardMod.MOD_ID,
                "textures/item/" + name + ".png"
        );
    }

    private static final Identifier DEFAULT =
            tex("aeriscard");

    private static final Identifier[] CARD = {
            tex("card_texture_1"),
            tex("card_texture_2"),
            tex("card_texture_3")
    };

    private static final Identifier[] TRUMP = {
            tex("trump_card_texture_1"),
            tex("trump_card_texture_2"),
            tex("trump_card_texture_3"),
            tex("trump_card_texture_4"),
            tex("trump_card_texture_5"),
            tex("trump_card_texture_6"),
            tex("trump_card_texture_7"),
            tex("trump_card_texture_8"),
            tex("trump_card_texture_9"),
            tex("trump_card_texture_10"),
            tex("trump_card_texture_11"),
            tex("trump_card_texture_12"),
            tex("trump_card_texture_13"),
            tex("trump_card_texture_14"),
            tex("trump_card_texture_15"),
            tex("trump_card_texture_16"),
            tex("trump_card_texture_17"),
            tex("trump_card_texture_18"),
            tex("trump_card_texture_19"),
            tex("trump_card_texture_20"),
            tex("trump_card_texture_21"),
            tex("trump_card_texture_22"),
            tex("trump_card_texture_23"),
            tex("trump_card_texture_24"),
            tex("trump_card_texture_25"),
            tex("trump_card_texture_26"),
            tex("trump_card_texture_27"),
            tex("trump_card_texture_28"),
            tex("trump_card_texture_29"),
            tex("trump_card_texture_30"),
            tex("trump_card_texture_31"),
            tex("trump_card_texture_32"),
            tex("trump_card_texture_33"),
            tex("trump_card_texture_34"),
            tex("trump_card_texture_35"),
            tex("trump_card_texture_36"),
            tex("trump_card_texture_37"),
            tex("trump_card_texture_38"),
            tex("trump_card_texture_39"),
            tex("trump_card_texture_40"),
            tex("trump_card_texture_41"),
            tex("trump_card_texture_42"),
            tex("trump_card_texture_43"),
            tex("trump_card_texture_44"),
            tex("trump_card_texture_45"),
            tex("trump_card_texture_46"),
            tex("trump_card_texture_47"),
            tex("trump_card_texture_48"),
            tex("trump_card_texture_49"),
            tex("trump_card_texture_50"),
            tex("trump_card_texture_51"),
            tex("trump_card_texture_52"),
            tex("trump_card_texture_53"),
            tex("trump_card_texture_54")
    };

    private static Identifier current = DEFAULT;

    private static Identifier[] currentPool = CARD;

    public static void rollRandomTexture() {
        ThreadLocalRandom r =
                ThreadLocalRandom.current();

        currentPool =
                r.nextInt(100) < 90
                        ? CARD
                        : TRUMP;

        current =
                currentPool[
                        r.nextInt(currentPool.length)
                        ];
    }

    public static void rerollWithinCurrentPool() {
        ThreadLocalRandom r =
                ThreadLocalRandom.current();

        current =
                currentPool[
                        r.nextInt(currentPool.length)
                        ];
    }

    public static Identifier currentTexture() {
        return current;
    }

    private AerisCardClientState() {
    }
}