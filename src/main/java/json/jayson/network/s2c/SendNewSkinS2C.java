package json.jayson.network.s2c;

import com.mojang.authlib.GameProfile;
import json.jayson.client.ClientSkinCache;
import json.jayson.client.StoneSkinTexture;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Base64;

public class SendNewSkinS2C {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        GameProfile gameProfile = packetByteBuf.readGameProfile();
        Identifier identifier = packetByteBuf.readIdentifier();
        StoneSkinTexture stoneSkinTexture = new StoneSkinTexture(identifier, gameProfile);
        stoneSkinTexture.setGrayScaled(Base64.getDecoder().decode(packetByteBuf.readString()));
        ClientSkinCache.addStoneSkin(gameProfile, stoneSkinTexture);
    }
}
