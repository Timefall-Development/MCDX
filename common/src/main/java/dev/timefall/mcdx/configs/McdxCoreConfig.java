package dev.timefall.mcdx.configs;

import dev.timefall.mcdx.ModConstants;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;

import java.util.Arrays;

public class McdxCoreConfig extends Config {

	public static final McdxCoreConfig INSTANCE = ConfigApiJava.registerAndLoadConfig(McdxCoreConfig::new);

	public McdxCoreConfig() {
		super(ModConstants.id("core_config"));
	}

	public ValidatedChoiceList<AoeExclusionType> aoeExclusions =  new ValidatedList<>(Arrays.stream(AoeExclusionType.values()).toList(), new ValidatedEnum<>(AoeExclusionType.SELF)).toChoiceList();

	public ValidatedBoolean aoeAffectsPlayers = new ValidatedBoolean();

}