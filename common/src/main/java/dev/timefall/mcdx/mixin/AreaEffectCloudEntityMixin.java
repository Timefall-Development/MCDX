package dev.timefall.mcdx.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.DataResult;
import dev.timefall.mcdx.configs.AoeExclusionType;
import dev.timefall.mcdx.interfaces.IExclusiveAOECloud;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin implements IExclusiveAOECloud {

	@Shadow @Nullable
	public abstract LivingEntity getOwner();

	@Unique
	private Set<AoeExclusionType> mcdx$exclusions = Set.of();

	@Override
	public Set<AoeExclusionType> mcdx$getExclusions() {
		return mcdx$exclusions;
	}

	@Override
	public void mcdx$setExclusions(Set<AoeExclusionType> types) {
		this.mcdx$exclusions = types;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void mcdx$nbtToTag(NbtCompound tag, CallbackInfo ci) {
		if (mcdx$exclusions.isEmpty()) return;
		DataResult<NbtElement> exclusionsResult =  AoeExclusionType.LIST_CODEC.encodeStart(NbtOps.INSTANCE, mcdx$exclusions.stream().toList());
		exclusionsResult.ifSuccess(exclusionsNbt -> tag.put("mcdx$exclusions", exclusionsNbt));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void mcdx$nbtFromTag(NbtCompound tag, CallbackInfo ci) {
		if (!tag.contains("mcdx$exclusions")) return;
		DataResult<List<AoeExclusionType>> exclusionsResult = AoeExclusionType.LIST_CODEC.parse(NbtOps.INSTANCE, tag.get("mcdx$exclusions"));
		exclusionsResult.ifSuccess(list -> mcdx$setExclusions(new HashSet<>(list)));
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/World.getNonSpectatingEntities (Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	private List<LivingEntity> mcdx$filterEntitiesByExclusions(World instance, Class<? extends LivingEntity> aClass, Box box, Operation<List<LivingEntity>> original) {
		List<LivingEntity> originalResult = original.call(instance, aClass, box);
		if (originalResult.isEmpty() || mcdx$exclusions.isEmpty()) return originalResult;
		LivingEntity thisOwner = this.getOwner();
		if (thisOwner == null) return originalResult;
		List<LivingEntity> filteredResult = new ArrayList<>();
		for (LivingEntity entity : originalResult) {
			if (AoeExclusionType.isExcluded(thisOwner, entity, mcdx$exclusions)) continue;
			filteredResult.add(entity);
		}
		return filteredResult;
	}
}