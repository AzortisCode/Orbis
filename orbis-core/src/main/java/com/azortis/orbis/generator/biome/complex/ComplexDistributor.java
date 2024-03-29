/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.azortis.orbis.generator.biome.complex;

import com.azortis.orbis.Registry;
import com.azortis.orbis.generator.biome.BiomeLayout;
import com.azortis.orbis.generator.biome.BiomeSection;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.complex.layer.NoiseLayer;
import com.azortis.orbis.generator.biome.complex.layer.RegionLayer;
import com.azortis.orbis.generator.biome.complex.modifier.Modifier;
import com.azortis.orbis.generator.biome.complex.modifier.RangedLinearModifier;
import com.azortis.orbis.generator.biome.complex.requirement.MaxStrength;
import com.azortis.orbis.generator.biome.complex.requirement.MinStrength;
import com.azortis.orbis.generator.biome.complex.requirement.NoiseRanges;
import com.azortis.orbis.generator.biome.complex.requirement.Requirement;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.Invoke;
import com.azortis.orbis.pack.data.Component;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@Inject
@Component(ComplexAccess.class)
public final class ComplexDistributor extends Distributor {

    public static Registry<Requirement> REGISTRY_REQUIREMENT = new Registry<>(Requirement.class, Map.of(
            Key.key("complex:noise_ranges"), NoiseRanges.class,
            Key.key("complex:minimum_strength"), MinStrength.class,
            Key.key("complex:maximum_strength"), MaxStrength.class
    ));
    public static Registry<Modifier> REGISTRY_MODIFIER = new Registry<>(Modifier.class, Map.of(
            Key.key("complex:ranged_linear"), RangedLinearModifier.class
    ));

    static {
        Registry.addRegistry(Requirement.class, REGISTRY_REQUIREMENT);
        Registry.addRegistry(Modifier.class, REGISTRY_MODIFIER);
    }

    private final Set<NoiseLayer> globalNoiseLayers;
    private final Set<RegionLayer> initialRegions;
    private final RegionLayer fallbackRegion;

    private ComplexDistributor(String name, Key providerKey, Set<NoiseLayer> globalNoiseLayers,
                               Set<RegionLayer> initialRegions, RegionLayer fallbackRegion) {
        super(name, providerKey);
        this.globalNoiseLayers = globalNoiseLayers;
        this.initialRegions = initialRegions;
        this.fallbackRegion = fallbackRegion;
    }

    @Invoke(when = Invoke.Order.PRE_INJECTION)
    private void setup() {

    }

    @Override
    protected @NotNull BiomeSection sample(int x, int z) {
        return null;
    }

    @Override
    protected @NotNull BiomeSection sample(int x, int y, int z) {
        return null;
    }

    @Override
    public @NotNull BiomeLayout layout() {
        return BiomeLayout.HYBRID;
    }
}
