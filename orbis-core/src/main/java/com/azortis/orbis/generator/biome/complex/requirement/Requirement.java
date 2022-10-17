/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.generator.biome.complex.requirement;

import com.azortis.orbis.Registry;
import com.azortis.orbis.pack.studio.annotations.GlobalDefinition;
import com.azortis.orbis.pack.studio.annotations.Typed;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Typed
@GlobalDefinition("complex-distributor-requirement")
public abstract class Requirement {

    public static final Registry<Requirement> REGISTRY = new Registry<>(Map.of(
            Key.key("complex:max-strength"), MaxStrength.class,
            Key.key("complex:min-strength"), MinStrength.class,
            Key.key("complex:noise-ranges"), NoiseRanges.class
    ));

    static {
        Registry.addRegistry(Requirement.class, REGISTRY);
    }

    protected final Key type;

    protected Requirement(Key type) {
        this.type = type;
    }

    public abstract boolean isAchieved(Map<String, Double> context);

    public abstract @NotNull Type getType();

    public enum Type {
        NOISE,
        STRENGTH
    }

}
