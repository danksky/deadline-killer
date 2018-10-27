package com.pherodev.killddl;

import com.pherodev.killddl.models.Task;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class TaskTests {
    @Test
    public void testTaskModelGetters() {
        String title = "testing title";
        String description = "testing description";
        int idt = 123;
        int idc = 321;
        Date deadline = new Date(2018,10,17,29,300,20);

        Task t = new Task(idt,idc,title,description,deadline);

        assertTrue(t.getDescription().equals(description));
        assertTrue(t.getTitle().equals(title));
        assertTrue(t.getId() == idt);
        assertTrue(t.getCategoryId() == idc);
        assertTrue(t.getDeadline().equals(deadline));
    }
}
