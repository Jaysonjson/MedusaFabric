package json.jayson.server;

import com.mojang.authlib.GameProfile;
import json.jayson.provider.StoneSkinProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.UUID;

public class ServerSkinCache {

    private static ArrayList<ServerStoneSkin> SKINS = new ArrayList<>();

    public static ServerStoneSkin addSkinToServerCache(Identifier identifier, GameProfile gameProfile) {
        try {
            ServerStoneSkin serverStoneSkin = new ServerStoneSkin();
            serverStoneSkin.skinData = StoneSkinProvider.stonifySkin(StoneSkinProvider.retrieveSkin(gameProfile));
            serverStoneSkin.gameProfile = gameProfile;
            serverStoneSkin.identifier = identifier;
            if (serverStoneSkin.skinData != null) {
                SKINS.add(serverStoneSkin);
            }
            return serverStoneSkin;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ServerStoneSkin getStoneSkin(UUID uuid) {
        for (ServerStoneSkin serverStoneSkin : getStoneSkinsServerCache()) {
            if(serverStoneSkin.gameProfile.getId().equals(uuid)) {
                return serverStoneSkin;
            }
        }
        return null;
    }

    public static ArrayList<ServerStoneSkin> getStoneSkinsServerCache() {
        return SKINS;
    }

    public static class ServerStoneSkin {
        public byte[] skinData;
        public Identifier identifier;
        public GameProfile gameProfile;
    }

}
