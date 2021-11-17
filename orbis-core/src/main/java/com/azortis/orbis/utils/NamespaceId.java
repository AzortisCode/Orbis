/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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

package com.azortis.orbis.utils;

import com.google.common.base.Preconditions;
import lombok.Getter;

public class NamespaceId {

    public static final String NAMESPACE_ID_REGEX = "^([a-z0-9._-]+):([a-z0-9._-]+)$";
    public static final String NAMESPACE_REGEX = "^[a-z0-9._-]+$";
    public static final String ID_REGEX = "^[a-z0-9._-]+$";

    @Getter
    private final String namespaceId;

    public NamespaceId(String namespaceId) {
        Preconditions.checkArgument(namespaceId.matches(NAMESPACE_ID_REGEX));
        this.namespaceId = namespaceId;
    }

    public NamespaceId(String namespace, String id) {
        Preconditions.checkArgument(namespace.matches(NAMESPACE_REGEX));
        Preconditions.checkArgument(namespace.matches(ID_REGEX));
        this.namespaceId = namespace + ":" + id;
    }

    public String getNamespace() {
        return namespaceId.split(":")[0];
    }

    public String getId() {
        return namespaceId.split(":")[1];
    }

    @Override
    public int hashCode() {
        return namespaceId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return namespaceId.equalsIgnoreCase((String) obj);
        } else if (obj instanceof NamespaceId) {
            return ((NamespaceId) obj).getNamespaceId().equalsIgnoreCase(namespaceId);
        }
        return false;
    }

    @Override
    public String toString() {
        return namespaceId;
    }
}
