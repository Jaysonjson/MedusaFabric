package json.jayson;

import json.jayson.network.MedusaNetwork;
import json.jayson.provider.StoneSkinProvider;
import json.jayson.server.ServerSkinCache;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Medusa implements ModInitializer {
	public static final String ID = "medusa";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static ModContainer CONTAINER;

	@Override
	public void onInitialize() {
		CONTAINER = FabricLoader.getInstance().getModContainer(ID).get();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity serverPlayerEntity = handler.getPlayer();
			ServerSkinCache.ServerStoneSkin stoneSkin = ServerSkinCache.addSkinToServerCache(StoneSkinProvider.getSkinIdentifier(serverPlayerEntity.getUuid()), serverPlayerEntity.getGameProfile());
			if(stoneSkin != null) {
				//MedusaNetwork.Server.sendSingletonSkin(serverPlayerEntity, stoneSkin);
				for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
					MedusaNetwork.Server.sendSingletonSkin(playerEntity, stoneSkin);
				}

				/* THIS IS JUST FOR TESTING; NEED TO MAKE IT BETTER BEFORE SPAMMING CLIENT WITH SKINS LMAO
				*  But generally, for small servers it should work
				*
				* */
				for (ServerSkinCache.ServerStoneSkin serverStoneSkin : ServerSkinCache.getStoneSkinsServerCache()) {
					MedusaNetwork.Server.sendSingletonSkin(serverPlayerEntity, serverStoneSkin);
				}
			}
		});

	}

	public static Identifier location(String lo) {
		return new Identifier(ID, lo);
	}
}