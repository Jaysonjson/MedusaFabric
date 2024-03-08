package json.jayson.client;

import com.mojang.authlib.GameProfile;
import json.jayson.provider.StoneSkinProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.UUID;

public class ClientSkinCache {

    private static HashMap<UUID, Identifier> STONE_SKINS = new HashMap<>();

    public static HashMap<UUID, Identifier> getStoneSkins() {
        return STONE_SKINS;
    }

    public static void addStoneSkin(GameProfile gameProfile, StoneSkinTexture stoneSkinTexture) {
        if(!getStoneSkins().containsKey(gameProfile.getId())) {
            MinecraftClient.getInstance().getTextureManager().registerTexture(StoneSkinProvider.getSkinIdentifier(gameProfile.getId()), stoneSkinTexture);
            MinecraftClient.getInstance().getTextureManager().bindTexture(StoneSkinProvider.getSkinIdentifier(gameProfile.getId()));
            getStoneSkins().put(gameProfile.getId(), StoneSkinProvider.getSkinIdentifier(gameProfile.getId()));
        }
    }

}
