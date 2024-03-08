package json.jayson.client;

import json.jayson.network.MedusaNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MedusaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MedusaNetwork.registerS2C();
    }
}
