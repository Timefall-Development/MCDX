package dev.timefall.mcdx.configs;

import com.mojang.serialization.Codec;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public enum AoeExclusionType implements EnumTranslatable, StringIdentifiable {
	SELF("self", (us, center, them) -> them == us),
	OTHER_PLAYER("other_player", (us, center, them) -> them.isPlayer()),
	TEAMMATE("teammate", (us, center, them) -> them.isTeammate(us)),
	SELF_PET("self_pet", (us, center, them) -> them instanceof Tameable pet && pet.getOwner() == us),
	ANY_PET("any_pet", (us, center, them) -> them instanceof Tameable pet && pet.getOwner() != null),
	ANIMAL("animal", (us, center, them) -> them instanceof AnimalEntity),
	VILLAGE("village", (us, center, them) -> them instanceof VillagerEntity || them instanceof IronGolemEntity || them instanceof AllayEntity),
	HOSTILE("hostile", (us, center, them) -> them instanceof Monster),
	AMBIENT("ambient", (us, center, them) -> them instanceof AmbientEntity),
	OBSCURED("obscured", (us, center, them) -> center instanceof LivingEntity le && !le.canSee(them));

	private final String id;
	private final ExclusionTest entityPredicate;

	AoeExclusionType(String id, ExclusionTest entityPredicate) {
		this.id = id;
		this.entityPredicate = entityPredicate;
	}

	public boolean isExcluded(Entity us, Entity center, Entity them) {
		return entityPredicate.test(us, center, them);
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

	public static boolean isExcluded(Entity us, Entity them, Collection<AoeExclusionType> exclusions) {
		for (AoeExclusionType exclusion : exclusions) {
			if (exclusion.isExcluded(us, us, them)) return true;
		}
		return false;
	}

	public static boolean isExcluded(Entity us, Entity center, Entity them, Collection<AoeExclusionType> exclusions) {
		for (AoeExclusionType exclusion : exclusions) {
			if (exclusion.isExcluded(us, center, them)) return true;
		}
		return false;
	}

	public static final List<AoeExclusionType> TYPES = Arrays.stream(AoeExclusionType.values()).toList();

	public static final Codec<AoeExclusionType> CODEC = StringIdentifiable.createCodec(AoeExclusionType::values);

	public static final Codec<List<AoeExclusionType>> LIST_CODEC = CODEC.listOf();

	@FunctionalInterface
	interface ExclusionTest {
		boolean test(Entity cause, Entity center, Entity target);
	}
}