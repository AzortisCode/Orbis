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

package com.azortis.orbis.paper.util;

import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * The Paper platform implementation for {@link Scheduler}.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public final class PaperScheduler implements Scheduler {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Task runTask(@NotNull Runnable task) {
        return new Task(Bukkit.getScheduler().runTask(OrbisPlugin.getPlugin(), task).getTaskId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Task runTaskAsync(@NotNull Runnable task) {
        return new Task(Bukkit.getScheduler().runTaskAsynchronously(OrbisPlugin.getPlugin(), task).getTaskId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scheduler.Task runDelayedTaskAsync(@NotNull Runnable task, long ticks) {
        return new Task(Bukkit.getScheduler().runTaskLaterAsynchronously(OrbisPlugin.getPlugin(), task, ticks).getTaskId());
    }

    /**
     * The Paper platform implementation for {@link Scheduler.Task}.
     *
     * @author Jake Nijssen
     * @since 0.3-Alpha
     */
    public static final class Task implements Scheduler.Task {
        private final int id;

        /**
         * Constructs a PaperTask from the given Paper internal taskId. See {@link BukkitTask#getTaskId()}
         *
         * @param id The paper internal task id.
         * @since 0.3-Alpha
         */
        private Task(int id) {
            this.id = id;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void cancel() {
            Bukkit.getScheduler().cancelTask(id);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isRunning() {
            return Bukkit.getScheduler().isCurrentlyRunning(id);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isQueued() {
            return Bukkit.getScheduler().isQueued(id);
        }
    }

}
