package dev.timefall.mcdx.api.armor;

import net.minecraft.entity.LivingEntity;

public class ArmorHelper {

    public static boolean checkSet(LivingEntity entity, EquipmentSet set) {
        return set.isSetEquipped(entity);
    }

}
