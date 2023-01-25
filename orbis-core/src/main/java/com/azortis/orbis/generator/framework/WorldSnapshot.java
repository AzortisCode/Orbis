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

package com.azortis.orbis.generator.framework;

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.util.annotations.ChunkCoords;
import com.azortis.orbis.world.World;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides a thread safe snapshot of a world where any chunk can be accessed generated or not.
 * This is needed for cross-chunk generation access for example when generating large features or structure objects.
 * {@link WorldStage}'s must first acquire a lock using {@link WorldSnapshot#acquire(int, int)} to gain access to the
 * chunk for this running thread, and once done release it using {@link WorldSnapshot#release(int, int)}. This must be
 * done for all chunks, even the one that initiated the generating task to ensure write safety.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public abstract class WorldSnapshot {

    private final Long2ObjectMap<ReentrantLock> locks = Long2ObjectMaps.synchronize(new Long2ObjectArrayMap<>(1024));

    protected final World world;
    protected final Dimension dimension;
    protected final Engine engine;

    public WorldSnapshot(@NotNull World world, @NotNull Dimension dimension, @NotNull Engine engine) {
        this.world = world;
        this.dimension = dimension;
        this.engine = engine;
    }

    @ChunkCoords
    public void acquire(int x, int z) {
        long key = (((long) x) << 32) | (z & 0xffffffffL);
        if (locks.containsKey(key)) {
            locks.get(key).lock();
        } else {
            ReentrantLock lock = new ReentrantLock(true);
            lock.lock();
            locks.put(key, lock);
        }
    }

    @ChunkCoords
    public void release(int x, int z) {
        long key = (((long) x) << 32) | (z & 0xffffffffL);
        if (locks.containsKey(key)) {
            ReentrantLock lock = locks.get(key);
            if (lock.hasQueuedThreads()) {
                locks.remove(key);
            }
            lock.unlock();
        }
    }

    public World world() {
        return world;
    }

    public Dimension dimension() {
        return dimension;
    }

    public Engine engine() {
        return engine;
    }
}
