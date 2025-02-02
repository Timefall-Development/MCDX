package dev.timefall.mcdx;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModConstants {

	public static final String MOD_ID = "mcdx";
	public static Identifier id(String path){
		return Identifier.of(MOD_ID, path);
	}
	public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
	public static final Logger DEV_LOG = ConfigApiJava.platform().devLogger(MOD_ID);
}