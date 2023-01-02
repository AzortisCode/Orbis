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

package com.azortis.orbis.pack.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializer;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;

/**
 * A class that manages implementation specific data types that aren't used globally.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
public abstract class ComponentAccess {

    protected final String name;
    protected final Class<?> parentType;
    protected final DataAccess dataAccess;

    /**
     * Construct a ComponentAccess object. You should never check on the nullity of name or dataAccess.
     * As for indexing/registering purposes a dummy instance of the ComponentAccess may be created, in order to call
     * {@link ComponentAccess#dataTypes()} & {@link ComponentAccess#adapters()}.
     *
     * @param name       The name of the instance
     * @param parentType The type of component, must be from {@link DataAccess#GENERATOR_TYPES}
     * @param dataAccess The {@link DataAccess} instance used in the tree
     * @since 0.3-Alpha
     */
    protected ComponentAccess(@NotNull String name, @NotNull Class<?> parentType, @NotNull DataAccess dataAccess) {
        this.name = name;
        this.parentType = parentType;
        this.dataAccess = dataAccess;
    }

    /**
     * Return a full data path of a type that this {@link ComponentAccess} instance manages.
     *
     * @param type The {@link Class} of the requested type.
     * @return A full data path on where to find the requested type.
     * @since 0.3-Alpha
     */
    @NotNull
    public String getDataPath(@NotNull Class<?> type) {
        return String.format("%s%s%s", dataAccess.getDataPath(parentType), name, getTypeDataPath(type));
    }

    /**
     * Return a folder data path, on where the configuration files for the specified type can be found.
     * Some examples may be:
     * <ul>
     *     <li>"/" -> Every file in the directory folder of this ComponentAccess is the specified type</li>
     *     <li>"/**" -> Same as above, but sub folders may be searched as well.</li>
     *     <li>"/type/" -> Every file in this folder is the specified type</li>
     *     <li>"/type/**" -> Same as above, but sub folders may be searched as well.</li>
     * </ul>
     * <p>
     * Core limitation is that no <b>single</b> path can contain multiple type of files. If using sub folders it assumes
     * that those only contain the specified type as well. Every path must begin and end with a "/" except if
     * the sub folders are searched as well.
     * <p>
     * All types passed here must be included in {@link ComponentAccess#dataTypes()}.
     *
     * @param type The {@link Class} of the requested type.
     * @return The data path where the specified type config files can be found.
     * @throws IllegalArgumentException If the type doesn't have a data path.
     * @since 0.3-Alpha
     */
    protected abstract @NotNull String getTypeDataPath(@NotNull Class<?> type) throws IllegalArgumentException;

    /**
     * Return a set of classes that are handled by this {@link ComponentAccess}. Should only contain
     * types that are hosted in separate files and have their own data path.
     *
     * @return A {@link ImmutableSet} of classes that are handled in this {@link ComponentAccess}
     * @since 0.3-Alpha
     */
    public abstract @NotNull @Unmodifiable Set<Class<?>> dataTypes();

    /**
     * A map of {@link JsonDeserializer} that need to be added to support typed based objects
     * on component data level.
     *
     * @return A map with the {@link Class} key as the type deserialized by the specified JsonDeserializer.
     * @since 0.3-Alpha
     */
    public abstract @NotNull @Unmodifiable Map<Class<?>, JsonDeserializer<?>> adapters();

}
