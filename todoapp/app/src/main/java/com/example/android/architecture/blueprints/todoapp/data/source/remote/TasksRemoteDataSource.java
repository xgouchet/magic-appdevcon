/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.data.source.remote;

import android.support.annotation.NonNull;

import com.example.android.architecture.blueprints.todoapp.data.Task;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class TasksRemoteDataSource implements TasksDataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private static final float NETWORK_ERROR_PROBABILITY = 50.0f / 100.0f;

    private final static Map<String, Task> TASKS_SERVICE_DATA;
    private static TasksRemoteDataSource INSTANCE;

    private final Random random = new Random();

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    // Prevent direct instantiation.
    private TasksRemoteDataSource() {
    }

    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    public void getTasks(TasksDataSource.GetTasksCallback callback) {
        try {
            simulateNetwork();
            callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
        } catch (IOException e) {
            callback.onDataNotAvailable();
        }
    }

    /**
     * Note: {@link TasksDataSource.GetTaskCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    public void getTask(@NonNull String taskId, TasksDataSource.GetTaskCallback callback) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);
        try {
            simulateNetwork();
            callback.onTaskLoaded(task);
        } catch (IOException e) {
            callback.onDataNotAvailable();
        }
    }

    private void simulateNetwork() throws IOException {
        // Simulate network by delaying the execution.
        try {
            Thread.sleep(SERVICE_LATENCY_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate network error
        if (random.nextFloat() < NETWORK_ERROR_PROBABILITY) {
            throw new IOException("Random error (sorry)");
        }
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        Task oldTask = TASKS_SERVICE_DATA.get(taskId);
        if (oldTask != null) {
            completeTask(oldTask);
            oldTask.releaseInstance();
        }
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        Task oldTask = TASKS_SERVICE_DATA.get(taskId);
        if (oldTask != null) {
            activateTask(oldTask);
            oldTask.releaseInstance();
        }
    }

    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
}