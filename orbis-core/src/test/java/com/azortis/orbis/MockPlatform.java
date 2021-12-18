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

package com.azortis.orbis;

import com.azortis.orbis.world.Container;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

public class MockPlatform implements Platform{

    private final File directory;

    public MockPlatform(){
        this.directory = new File(System.getProperty("user.dir") + "/test-data/");
    }

    @Override
    public String getAdaptation() {
        return "Test";
    }

    @Override
    public org.slf4j.Logger getLogger() {
        return LoggerFactory.getLogger(BlocksTest.class);
    }

    @Override
    public File getDirectory() {
        return this.directory;
    }

    @Override
    public @org.jetbrains.annotations.Nullable Container getContainer(String name) {
        return null;
    }

    @Override
    public Collection<Container> getContainers() {
        return null;
    }
}
