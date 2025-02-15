package dev.timefall.mcdx.configs;

import dev.timefall.mcdx.ModConstants;
import dev.timefall.mcdx.configs.sections.McdxLootConfigSection;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;

import java.util.Arrays;
import java.util.List;

public class McdxCoreConfig extends Config {

	public static final McdxCoreConfig INSTANCE = ConfigApiJava.registerAndLoadConfig(McdxCoreConfig::new);

	public McdxCoreConfig() {
		super(ModConstants.id("core_config"));
	}

	public ValidatedChoiceList<AoeExclusionType> aoeExclusions = new ValidatedList<>(
			List.of(
					AoeExclusionType.SELF,
					AoeExclusionType.OBSCURED
			),
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(AoeExclusionType.TYPES);

	public ValidatedChoiceList<AoeExclusionType> allyExclusions = new ValidatedList<>(
			List.of(
					AoeExclusionType.SELF,
					AoeExclusionType.SELF_PET,
					AoeExclusionType.VILLAGE,
					AoeExclusionType.TEAMMATE,
					AoeExclusionType.OBSCURED
			),
			new ValidatedEnum<>(AoeExclusionType.SELF)
	).toChoiceList(AoeExclusionType.TYPES);

	public McdxLootConfigSection lootConfigSection = new McdxLootConfigSection();

}