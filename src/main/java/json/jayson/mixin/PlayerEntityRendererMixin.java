package json.jayson.mixin;

import json.jayson.client.ClientSkinCache;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {


	@Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void getTextureAbstractPlayer(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir) {
		if(abstractClientPlayerEntity.hasStatusEffect(StatusEffects.BAD_OMEN) && ClientSkinCache.getStoneSkins().containsKey(abstractClientPlayerEntity.getUuid())) {
			cir.setReturnValue(ClientSkinCache.getStoneSkins().get(abstractClientPlayerEntity.getUuid()));
		}
	}

	/*@Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void getTextureEntity(CallbackInfoReturnable<Identifier> info) {
		if(ClientSkinCache.getStoneSkins().containsKey(MinecraftClient.getInstance().player.getUuid())) {
			info.setReturnValue(ClientSkinCache.getStoneSkins().get(MinecraftClient.getInstance().player.getUuid()));
		}
	}*/

	@ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
	private Identifier injected(Identifier x) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if(player.hasStatusEffect(StatusEffects.BAD_OMEN) && ClientSkinCache.getStoneSkins().containsKey(player.getUuid())) {
			return ClientSkinCache.getStoneSkins().get(player.getUuid());
		}
		return x;
	}
}