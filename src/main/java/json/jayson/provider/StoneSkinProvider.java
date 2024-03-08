package json.jayson.provider;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import json.jayson.Medusa;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

public class StoneSkinProvider {

    public static URL retrieveSkin(GameProfile gameProfile) throws MalformedURLException {
        for (Property textures : gameProfile.getProperties().get("textures")) {
            String value = textures.value();
            String skinJson = new String(Base64.getDecoder().decode(value));
            SkinData skinData = new Gson().fromJson(skinJson, SkinData.class);
            return new URL(skinData.textures.SKIN.url);
        }
        return null;
    }

    public static byte[] stonifySkin(URL url) {
        try {
            BufferedImage bufferedImage = ImageIO.read(url);
            Path texturePath = Medusa.CONTAINER.findPath("assets/medusa/textures/stone_overlay.png").get();
            InputStream inputStream = Files.newInputStream(texturePath);
            BufferedImage stoneOverlay = ImageIO.read(inputStream);
            for (int i = 0; i < bufferedImage.getWidth(); i++) {
                for (int j = 0; j < bufferedImage.getHeight(); j++) {
                    int currentRgb = bufferedImage.getRGB(i,j);
                    Color color = new Color(currentRgb);
                    int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int finalized = (currentRgb & 0xff000000) | (gray << 16) | (gray << 8) | gray;
                    bufferedImage.setRGB(i, j, finalized);
                }
            }
            Graphics2D g = bufferedImage.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.4f));
            g.drawImage(stoneOverlay, (bufferedImage.getWidth() - stoneOverlay.getWidth()) / 2, (bufferedImage.getHeight() - stoneOverlay.getHeight()) / 2, null);
            g.dispose();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            new FileOutputStream("test.png").write(bytes);
            byteArrayOutputStream.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Identifier getSkinIdentifier(UUID uuid) {
        return Medusa.location("skin/" + uuid.toString().replaceAll("-",""));
    }

}
