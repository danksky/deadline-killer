package com.pherodev.killddl.adapters;

import com.pherodev.killddl.models.Task;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Task> {

    public int compare(Task a, Task b) {
        return a.getPriority() - b.getPriority();
    }
}