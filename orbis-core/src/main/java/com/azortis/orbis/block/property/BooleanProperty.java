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

package com.azortis.orbis.block.property;

import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A {@link Property} that can be either true or false.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public final class BooleanProperty extends Property<Boolean> {

    public static final Set<Boolean> VALUES = Set.of(true, false);

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    private BooleanProperty(final @NotNull String key) {
        super(key, Boolean.class, VALUES);
    }

    @Contract("_ -> new")
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    static @NotNull BooleanProperty create(final @NotNull String key) {
        return new BooleanProperty(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Boolean getValue(@NotNull String value) {
        final boolean val = Boolean.parseBoolean(value);
        if (!values().contains(val)) {
            throw new IllegalArgumentException("Invalid boolean value: " + value + ". Must be in " + values());
        }
        return val;
    }

}
