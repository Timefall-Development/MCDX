package dev.timefall.mcdx.configs;

import com.mojang.serialization.Codec;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public enum AoeExclusionType implements EnumTranslatable, StringIdentifiable {
	SELF("self", (us, them) -> them == us),
	OTHER_PLAYER("other_player", (us, them) -> them.isPlayer()),
	TEAMMATE("teammate", Entity::isTeammate),
	SELF_PET("self_pet", (us, them) -> them instanceof Tameable pet && pet.getOwner() == us),
	ANY_PET("any_pet", (us, them) -> them instanceof Tameable pet && pet.getOwner() != null),
	ANIMAL("animal", (us, them) -> them instanceof AnimalEntity),
	VILLAGE("village", (us, them) -> them instanceof VillagerEntity || them instanceof IronGolemEntity || them instanceof AllayEntity),
	HOSTILE("hostile", (us, them) -> them instanceof Monster);

	private final String id;
	private final BiPredicate<Entity, Entity> entityPredicate;

	AoeExclusionType(String id, BiPredicate<Entity, Entity> entityPredicate) {
		this.id = id;
		this.entityPredicate = entityPredicate;
	}

	public boolean isExcluded(Entity us, Entity them) {
		return entityPredicate.test(us, them);
	}

	@Override
	@NotNull
	public String prefix() {
		return "mcdx.enum.aoe_exclusion";
	}

	@Override
	public String asString() {
		return id;
	}

	public static boolean applies(Entity us, Entity them, Collection<AoeExclusionType> exclusions) {
		for (AoeExclusionType exclusion : exclusions) {
			if (exclusion.isExcluded(us, them)) return false;
		}
		return true;
	}

	public static final Codec<AoeExclusionType> CODEC = StringIdentifiable.createCodec(AoeExclusionType::values);

	public static final Codec<List<AoeExclusionType>> LIST_CODEC = CODEC.listOf();
}