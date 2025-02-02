/*
 * Timefall Development License 1.2
 * Copyright (c) 2020-2024. Chronosacaria, Kluzzio, Timefall Development. All Rights Reserved.
 *
 * This software's content is licensed under the Timefall Development License 1.2. You can find this license information here: https://github.com/Timefall-Development/Timefall-Development-Licence/blob/main/TimefallDevelopmentLicense1.2.txt
 */
package dev.timefall.mcdx.interfaces;

import dev.timefall.mcdx.configs.AoeExclusionType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface IExclusiveAOECloud {

    Set<AoeExclusionType> mcdx$getExclusions();
    void mcdx$setExclusions(Set<AoeExclusionType> types);

    default void mcdx$setExclusions(AoeExclusionType... exclusionTypes) {
        mcdx$setExclusions(Arrays.stream(exclusionTypes).collect(Collectors.toSet()));
    }
}