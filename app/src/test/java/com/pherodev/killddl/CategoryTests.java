package com.pherodev.killddl;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.pherodev.killddl.models.Category;
import com.pherodev.killddl.models.Task;

import java.util.Date;

public class CategoryTests {
    @Test
    public void createCategoryUsingConstructor() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category(ide,title);
        assertTrue(c.getTitle().equals(title));
        assertTrue(c.getId() == ide);
    }

    @Test
    public void editCategoryData() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category(ide,title);
        String newTitle = "new title";
        c.setTitle(newTitle);
        assertTrue(c.getTitle().equals(newTitle));
        assertTrue(c.getId() == ide);
    }


    @Test
    public void editCategoryId() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category(ide,title);
        int ideNew = 1234;
        c.setId(ideNew);
        assertTrue(c.getTitle().equals(title));
        assertTrue(c.getId() == ideNew);
    }

    @Test
    public void getCategoryData() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category();
        c.setId(ide);
        c.setTitle(title);
        assertTrue(c.getTitle().equals(title));
        assertTrue(c.getId() == ide);
    }

    @Test
    public void addTaskToCategory(){
        String title = "testing title";
        String description = "testing description";
        int idt = 123;
        int idc = 321;
        Date deadline = new Date(2018,10,17,29,300,20);

        Task t = new Task(idt,idc,title,description,deadline);

        String categoryTitle = "testing title";
        int ide = 123;
        Category c = new Category();
        c.setId(ide);
        c.setTitle(categoryTitle);

        c.list.add(t);

        assertTrue(c.list.contains(t));
    }

    @Test
    public void getTaskFromCategory(){
        String title = "testing title";
        String description = "testing description";
        int idt = 123;
        int idc = 321;
        Date deadline = new Date(2018,10,17,29,300,20);

        Task t = new Task(idt,idc,title,description,deadline);

        String categoryTitle = "testing title";
        int ide = 123;
        Category c = new Category();
        c.setId(ide);
        c.setTitle(categoryTitle);

        c.list.add(t);

        Task tGot = c.list.get(0);

        assertTrue(tGot == t);
    }

    @Test
    public void deleteTaskFromCategory(){
        String title = "testing title";
        String description = "testing description";
        int idt = 123;
        int idc = 321;
        Date deadline = new Date(2018,10,17,29,300,20);

        Task t = new Task(idt,idc,title,description,deadline);

        String categoryTitle = "testing title";
        int ide = 123;
        Category c = new Category();
        c.setId(ide);
        c.setTitle(categoryTitle);

        c.list.add(t);
        c.list.remove(t);

        assertTrue(c.list.size() == 0);
    }
}
