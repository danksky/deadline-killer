package com.pherodev.killddl;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.pherodev.killddl.models.Category;

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
}
