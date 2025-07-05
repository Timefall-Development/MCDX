package dev.timefall.mcdx.configs;

import dev.timefall.mcdx.ModConstants;
import dev.timefall.mcdx.configs.sections.McdxLootConfigSection;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;

import java.util.List;

public class McdxCoreConfig extends Config {

	public static final McdxCoreConfig INSTANCE = ConfigApiJava.registerAndLoadConfig(McdxCoreConfig::new);

	public McdxCoreConfig() {
		super(ModConstants.id("core_config"));
	}

	// Illager Artifacts will, generally, be AOE Exclusive
	public ValidatedChoiceList<AoeExclusionType> aoeExclusions = new ValidatedList<>(
			AoeExclusionType.TYPES,
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(
			List.of(
					AoeExclusionType.SELF,
					AoeExclusionType.CREATIVE_PLAYER,
					AoeExclusionType.OBSCURED
			)
	);

	// Illager Artifacts will, generally, be AOE Exclusive
	public ValidatedChoiceList<AoeExclusionType> selfHitExclusions = new ValidatedList<>(
			AoeExclusionType.TYPES,
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(
			List.of(
					AoeExclusionType.SELF,
					AoeExclusionType.CREATIVE_PLAYER,
					AoeExclusionType.SELF_PET,
					AoeExclusionType.OBSCURED
			)
	);

	// Villager Artifacts will, generally, be Ally Exclusive
	public ValidatedChoiceList<AoeExclusionType> allyExclusions = new ValidatedList<>(
			AoeExclusionType.TYPES,
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(
			List.of(
					AoeExclusionType.SELF,
					AoeExclusionType.CREATIVE_PLAYER,
					AoeExclusionType.SELF_PET,
					AoeExclusionType.VILLAGE,
					AoeExclusionType.TEAMMATE,
					AoeExclusionType.OBSCURED
	));

	// If an AOE can affect self
	public ValidatedChoiceList<AoeExclusionType> obscuredExclusions = new ValidatedList<>(
			AoeExclusionType.TYPES,
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(List.of(
			AoeExclusionType.CREATIVE_PLAYER,
			AoeExclusionType.OBSCURED
	));

	public McdxLootConfigSection lootConfigSection = new McdxLootConfigSection();

}