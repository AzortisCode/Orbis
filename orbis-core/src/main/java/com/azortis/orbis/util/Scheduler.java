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

package com.azortis.orbis.util;

import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for scheduling operations using the platforms in-house scheduler.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public interface Scheduler {

    /**
     * Runs a task on the main application thread of the platform.
     *
     * @param task The task to perform.
     * @return An instance of {@link Task} for the queued task.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    @NotNull
    Task runTask(@NotNull Runnable task);

    /**
     * Runs a task asynchronously on the platform's scheduler.
     *
     * @param task The task to perform.
     * @return An instance of {@link Task} for the queued task.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    @NotNull
    Task runTaskAsync(@NotNull Runnable task);

    /**
     * Runs a task asynchronously on the platform's scheduler after set delay.
     *
     * @param task  The task to perform.
     * @param ticks The delay in ticks.
     * @return An instance of {@link Task} for the queued task.
     * @since 0.3-Alpha
     */
    @Contract("_,_ -> new")
    @NotNull
    Task runDelayedTaskAsync(@NotNull Runnable task, long ticks);

    /**
     * A representation of a queued or running task, which can be cancelled.
     *
     * @author Jake Nijssen
     * @since 0.3-Alpha
     */
    interface Task {

        /**
         * Cancels the task.
         */
        @Contract(pure = true)
        void cancel();

        /**
         * Checks if the task is running.
         *
         * @return If the task is running.
         */
        @Contract(pure = true)
        boolean isRunning();

        /**
         * Checks if the task is queued for later.
         *
         * @return If the task is queued for later.
         */
        boolean isQueued();
    }

}
