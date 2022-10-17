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

package com.azortis.orbis.pack.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.util.Scheduler;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * A file event watcher that posts all external file changes to the {@link Project} for it
 * to process them, i.e. re-indexing all the files to point the right schema's to it.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.pack.studio")
public final class ProjectWatcher {

    private final WatchService watcher = FileSystems.getDefault().newWatchService();
    private final Map<WatchKey, Path> keys = new HashMap<>();
    private final Project project;

    private final Scheduler.Task watcherTask;
    private volatile boolean continueLoop = true;

    public ProjectWatcher(final @NotNull Project project) throws IOException {
        this.project = project;

        // Register Project root
        Path rootDir = project.directory().toPath();
        registerAll(rootDir);

        this.watcherTask = Orbis.getPlatform().scheduler().runTaskAsync(this::processEvents);
    }

    private void processEvents(){
        while(continueLoop) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                continueLoop = false;
                break;
            }

            Path dir = keys.get(key);
            if(dir == null) {
                Orbis.getLogger().error("WatchKey not recognized!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                WatchEvent<Path> pathEvent = cast(event);
                Path name = pathEvent.context();
                Path child = dir.resolve(name);

                try {
                    if (kind == ENTRY_CREATE) {
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                            registerAll(child);
                            project.onDirectoryCreate(child);
                        } else {
                            project.onFileCreate(child);
                        }
                    } else if (kind == ENTRY_MODIFY) {
                        if (Files.isRegularFile(child, LinkOption.NOFOLLOW_LINKS)) {
                            project.onFileModify(child);
                        }
                    } else if (kind == ENTRY_DELETE) {
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                            project.onDirectoryDelete(child);
                        } else {
                            project.onFileDelete(child);
                        }
                    }
                } catch (InterruptedException ex) {
                    Orbis.getLogger().error("Got interrupted while processing file event!", ex);
                }
            }

            boolean valid = key.reset();
            if(!valid) {
                keys.remove(key);

                if(keys.isEmpty()) {
                    terminate();
                    break;
                }
            }
        }
    }

    synchronized void terminate() {
        if(!continueLoop) {
            try {
                continueLoop = false;
                watcher.close();
                if(watcherTask.isRunning())watcherTask.cancel();
            } catch (IOException ex) {
                Orbis.getLogger().error("Failed to cancel ProjectWatcher!", ex);
            }
        }
    }

    private void register(@NotNull Path dir) {
        WatchKey key = null;
        try {
            key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException ex){
            Orbis.getLogger().error("Failed to register directory with the watcher!", ex);
        }
        if(key != null)keys.put(key, dir);
    }

    private void registerAll(@NotNull Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // We don't want to register the /.orbis/ directory since that dir is updated
                    // by events that happen in this watcher.
                    if(Files.isSameFile(project.settingsDir().toPath(), dir)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    register(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to register directory tree with watcher!", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

}
