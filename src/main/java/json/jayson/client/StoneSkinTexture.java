package json.jayson.client;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import json.jayson.Medusa;
import json.jayson.provider.StoneSkinProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

public class StoneSkinTexture extends ResourceTexture {
    GameProfile gameProfile;
    URL skinURL = null;
    byte[] grayScaled = null;

    public StoneSkinTexture(Identifier location, GameProfile gameProfile) {
        super(location);
        this.gameProfile = gameProfile;
    }

    public void setGrayScaled(byte[] grayScaled) {
        this.grayScaled = grayScaled;
    }

    private void onTextureLoaded(NativeImage image) {
        MinecraftClient.getInstance().execute(() -> {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> {
                    this.uploadTexture(image);
                });
            } else {
                this.uploadTexture(image);
            }

        });
    }

    private void uploadTexture(NativeImage image) {
        TextureUtil.prepareImage(this.getGlId(), image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, true);
    }


    public void load(ResourceManager manager) {
        try {
            if(grayScaled != null) {
                MinecraftClient.getInstance().execute(() -> {
                    NativeImage nativeImage = this.loadTexture(grayScaled);
                    if (nativeImage != null) {
                        onTextureLoaded(nativeImage);
                    }
                });
            } else {
                if(skinURL == null) {
                    skinURL = StoneSkinProvider.retrieveSkin(gameProfile);
                }
                if (skinURL != null) {
                    if(grayScaled == null) grayScaled = StoneSkinProvider.stonifySkin(skinURL);
                    MinecraftClient.getInstance().execute(() -> {
                        NativeImage nativeImage = this.loadTexture(grayScaled);
                        if (nativeImage != null) {
                            onTextureLoaded(nativeImage);
                        }
                    });
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private NativeImage loadTexture(byte[] bytes) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(bytes);

        } catch (Exception var4) {
            Exception exception = var4;
            Medusa.LOGGER.warn("Error while loading the stone skin texture", exception);
        }
        return nativeImage;
    }


}
