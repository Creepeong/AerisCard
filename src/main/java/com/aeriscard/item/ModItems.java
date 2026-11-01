package com.aeriscard.item;

import com.aeriscard.AerisCardMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import java.util.function.Function;

public final class ModItems {
    public static final ResourceKey<Item> AERIS_CARD_KEY =
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AerisCardMod.MOD_ID, "aeriscard"));
    public static final AerisCardItem AERIS_CARD =
            register(AERIS_CARD_KEY, AerisCardItem::new, new Item.Properties().stacksTo(1));

    private static <T extends Item> T register(ResourceKey<Item> key, Function<Item.Properties, T> factory, Item.Properties props) {
        T item = factory.apply(props.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    public static void initialize() {}
    private ModItems() {}
}