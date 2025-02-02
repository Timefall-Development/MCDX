package dev.timefall.mcdx.api;

import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupedObjectHelper {

    // Consider expanding loot tables to be able to seek out modded loot tables, such as BetterEnd

    public static final Set<Identifier> ILLAGER_GENERAL_LOOT_TABLES =
            Set.of(LootTables.WOODLAND_MANSION_CHEST, LootTables.PILLAGER_OUTPOST_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> VILLAGER_GENERAL_LOOT_TABLES =
            Set.of(LootTables.VILLAGE_ARMORER_CHEST, LootTables.VILLAGE_BUTCHER_CHEST,
                    LootTables.VILLAGE_CARTOGRAPHER_CHEST, LootTables.VILLAGE_FISHER_CHEST,
                    LootTables.VILLAGE_FLETCHER_CHEST, LootTables.VILLAGE_DESERT_HOUSE_CHEST,
                    LootTables.VILLAGE_MASON_CHEST, LootTables.VILLAGE_PLAINS_CHEST,
                    LootTables.VILLAGE_SAVANNA_HOUSE_CHEST, LootTables.VILLAGE_SHEPARD_CHEST,
                    LootTables.VILLAGE_SNOWY_HOUSE_CHEST, LootTables.VILLAGE_TAIGA_HOUSE_CHEST,
                    LootTables.VILLAGE_TANNERY_CHEST, LootTables.VILLAGE_TEMPLE_CHEST,
                    LootTables.VILLAGE_TOOLSMITH_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> VILLAGER_ILLAGER_DUNGEON_LOOT_TABLES =
            Set.of(LootTables.ABANDONED_MINESHAFT_CHEST, LootTables.SIMPLE_DUNGEON_CHEST, LootTables.SHIPWRECK_TREASURE_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    //////////////////////////

    public static final Set<Identifier> COMMON_LOOT_TABLES =
            Set.of(LootTables.ABANDONED_MINESHAFT_CHEST, LootTables.SHIPWRECK_SUPPLY_CHEST,
                    LootTables.SHIPWRECK_TREASURE_CHEST, LootTables.DESERT_PYRAMID_CHEST,
                    LootTables.VILLAGE_WEAPONSMITH_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> UNCOMMON_LOOT_TABLES =
            Set.of(LootTables.JUNGLE_TEMPLE_CHEST, LootTables.NETHER_BRIDGE_CHEST,
                    LootTables.BASTION_BRIDGE_CHEST, LootTables.BASTION_OTHER_CHEST,
                    LootTables.BASTION_TREASURE_CHEST, LootTables.RUINED_PORTAL_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> RARE_LOOT_TABLES =
            Set.of(LootTables.UNDERWATER_RUIN_SMALL_CHEST, LootTables.UNDERWATER_RUIN_BIG_CHEST,
                    LootTables.RUINED_PORTAL_CHEST, LootTables.SIMPLE_DUNGEON_CHEST,
                    LootTables.IGLOO_CHEST_CHEST, LootTables.PILLAGER_OUTPOST_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> EPIC_LOOT_TABLES =
            Set.of(LootTables.STRONGHOLD_CORRIDOR_CHEST, LootTables.STRONGHOLD_CROSSING_CHEST,
                    LootTables.STRONGHOLD_LIBRARY_CHEST, LootTables.END_CITY_TREASURE_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> VOID_LOOT_TABLES =
            Set.of(LootTables.END_CITY_TREASURE_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    ////////////////////////

    public static final Set<Identifier> PIGLIN_LOOT_TABLES =
            Set.of(LootTables.BASTION_BRIDGE_CHEST,
                LootTables.BASTION_HOGLIN_STABLE_CHEST, LootTables.BASTION_OTHER_CHEST, LootTables.BASTION_TREASURE_CHEST)
                .stream()
                .map(RegistryKey::getValue)
                .collect(Collectors.toSet());

    public static final Set<Identifier> PIGLIN_TRADING_LOOT_TABLES =
            Set.of(LootTables.PIGLIN_BARTERING_GAMEPLAY)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> NETHER_FORTRESS_LOOT_TABLES =
            Set.of(LootTables.NETHER_BRIDGE_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> VILLAGE_SMITH_LOOT_TABLES =
            Set.of(LootTables.VILLAGE_ARMORER_CHEST, LootTables.VILLAGE_WEAPONSMITH_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());

    public static final Set<Identifier> SUNKEN_SHIP_LOOT_TABLES =
            Set.of(LootTables.SHIPWRECK_TREASURE_CHEST, LootTables.SHIPWRECK_SUPPLY_CHEST)
                .stream()
                .map(RegistryKey::getValue)
                .collect(Collectors.toSet());

    public static final Set<Identifier> MINESHAFT_LOOT_TABLES =
            Set.of(LootTables.ABANDONED_MINESHAFT_CHEST)
                .stream()
                .map(RegistryKey::getValue)
                .collect(Collectors.toSet());

    public static final Set<Identifier> HERO_OF_THE_VILLAGE_LOOT_TABLES =
            Set.of(LootTables.HERO_OF_THE_VILLAGE_ARMORER_GIFT_GAMEPLAY)
                .stream()
                .map(RegistryKey::getValue)
                .collect(Collectors.toSet());

    public static final Set<Identifier> STRONGHOLD_LOOT_TABLES =
            Set.of(LootTables.STRONGHOLD_CORRIDOR_CHEST, LootTables.STRONGHOLD_CROSSING_CHEST, LootTables.STRONGHOLD_LIBRARY_CHEST)
                    .stream()
                    .map(RegistryKey::getValue)
                    .collect(Collectors.toSet());
}
