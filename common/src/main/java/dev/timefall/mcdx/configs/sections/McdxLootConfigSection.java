package dev.timefall.mcdx.configs.sections;

import dev.timefall.mcdx.api.GroupedObjectHelper;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedSet;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class McdxLootConfigSection extends ConfigSection {

    private ValidatedSet<Identifier> villagerLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_villager_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.VILLAGER_GENERAL_LOOT_TABLES);

    private ValidatedSet<Identifier> illagerLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_illager_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.ILLAGER_GENERAL_LOOT_TABLES);

    private ValidatedSet<Identifier> dungeonLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_dungeon_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.VILLAGER_ILLAGER_DUNGEON_LOOT_TABLES);

    private ValidatedSet<Identifier> commonLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_common_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.COMMON_LOOT_TABLES);

    private ValidatedSet<Identifier> uncommonLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_uncommon_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.UNCOMMON_LOOT_TABLES);

    private ValidatedSet<Identifier> rareLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_rare_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.RARE_LOOT_TABLES);

    private ValidatedSet<Identifier> epicLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_epic_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.EPIC_LOOT_TABLES);

    private ValidatedSet<Identifier> voidLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_void_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.VOID_LOOT_TABLES);

    private ValidatedSet<Identifier> piglinLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_piglin_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.PIGLIN_LOOT_TABLES);

    private ValidatedSet<Identifier> piglinBarteringLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_piglin_bartering_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.PIGLIN_TRADING_LOOT_TABLES);

    private ValidatedSet<Identifier> netherFortressLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_nether_fortress_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.NETHER_FORTRESS_LOOT_TABLES);

    private ValidatedSet<Identifier> smithLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_smith_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.VILLAGE_SMITH_LOOT_TABLES);

    private ValidatedSet<Identifier> sunkenShipLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_sunken_ship_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.SUNKEN_SHIP_LOOT_TABLES);

    private ValidatedSet<Identifier> mineshaftLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_mineshaft_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.MINESHAFT_LOOT_TABLES);

    private ValidatedSet<Identifier> heroOfTheVillageLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_hero_of_the_village_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.HERO_OF_THE_VILLAGE_LOOT_TABLES);

    private ValidatedSet<Identifier> strongholdLootTables = ValidatedIdentifier.ofDynamicKey(
            RegistryKeys.LOOT_TABLE,
            "mcdx_stronghold_tables",
            (id, entry) -> true).toSet(GroupedObjectHelper.STRONGHOLD_LOOT_TABLES);
}
