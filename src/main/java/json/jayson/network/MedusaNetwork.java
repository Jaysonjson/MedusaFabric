package json.jayson.network;

import json.jayson.Medusa;
import json.jayson.network.s2c.SendNewSkinS2C;
import json.jayson.provider.StoneSkinProvider;
import json.jayson.server.ServerSkinCache;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Base64;

public class MedusaNetwork {

    public static final Identifier SEND_SINGLETON_SKIN = new Identifier(Medusa.ID, "send_singleton");

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SEND_SINGLETON_SKIN, SendNewSkinS2C::receive);
    }

    public static void registerC2S() {
    }

    public static class Client {

    }

    public static class Server {

        public static void sendSingletonSkin(ServerPlayerEntity player) {
            ServerSkinCache.ServerStoneSkin stoneSkin = ServerSkinCache.getStoneSkin(player.getUuid());
            if(stoneSkin != null) {
                PacketByteBuf byteBufs = PacketByteBufs.create();
                byteBufs.writeGameProfile(stoneSkin.gameProfile);
                byteBufs.writeIdentifier(stoneSkin.identifier);
                byteBufs.writeBytes(stoneSkin.skinData);
                ServerPlayNetworking.send(player, SEND_SINGLETON_SKIN, byteBufs);
            }
        }

        public static void sendSingletonSkin(ServerPlayerEntity player, ServerSkinCache.ServerStoneSkin stoneSkin) {
            if(stoneSkin != null) {
                PacketByteBuf byteBufs = PacketByteBufs.create();
                byteBufs.writeGameProfile(stoneSkin.gameProfile);
                byteBufs.writeIdentifier(stoneSkin.identifier);
                byteBufs.writeString(new String(Base64.getEncoder().encode(stoneSkin.skinData)));
                ServerPlayNetworking.send(player, SEND_SINGLETON_SKIN, byteBufs);
            }
        }

    }
}
