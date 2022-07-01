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

package com.azortis.orbis.util.maven;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * Represents a dependency that a plugin requires to function.
 */
@Documented
@Repeatable(Dependencies.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    /**
     * The group ID of the dependency.
     *
     * @return The group ID of the dependency.
     */
    @Nonnull
    String group();

    /**
     * The artifact ID of the dependency.
     *
     * @return The artifact ID of the dependency.
     */
    @Nonnull
    String artifact();

    /**
     * The version of the dependency.
     *
     * @return The version of the dependency.
     */
    @Nonnull
    String version();

    /**
     * The repository of the dependency.
     *
     * @return The repository of the dependency.
     */
    @Nonnull
    Repository repository() default @Repository(url = "https://repo.maven.apache.org/maven2/");

    /**
     * The type of dependency.
     *
     * @return The type of dependency.
     */
    @Nonnull
    DependencyType type() default DependencyType.REQUIRED;

    /**
     * The type of dependency.
     */
    enum DependencyType {
        /**
         * A dependency that is required to function.
         */
        REQUIRED,
        /**
         * A dependency that is optional to function.
         */
        OPTIONAL
    }
}
