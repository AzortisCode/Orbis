/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.util;

import com.google.common.base.Preconditions;

public class NamespaceId {

    public static final String NAMESPACE_ID_REGEX = "^([a-z0-9._-]+):([a-z0-9._-]+)$";
    public static final String NAMESPACE_REGEX = "^[a-z0-9._-]+$";
    public static final String ID_REGEX = "^[a-z0-9._-]+$";

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

    public String getNamespaceId() {
        return namespaceId;
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
        if(obj instanceof String){
            return namespaceId.equalsIgnoreCase((String) obj);
        } else if(obj instanceof NamespaceId){
            return ((NamespaceId) obj).getNamespaceId().equalsIgnoreCase(namespaceId);
        }
        return false;
    }

    @Override
    public String toString() {
        return namespaceId;
    }
}
