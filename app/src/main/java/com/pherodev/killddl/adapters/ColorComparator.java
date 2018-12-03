package com.pherodev.killddl.adapters;

import com.pherodev.killddl.models.Task;

import java.util.Comparator;

public class ColorComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getColor() - o2.getColor();
    }
}
