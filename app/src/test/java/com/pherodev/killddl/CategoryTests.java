package com.pherodev.killddl;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;
import com.pherodev.killddl.models.Category;

public class CategoryTests {
    @Test
    public void testCategoryModelGetters() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category(ide,title);
        assertTrue(c.getTitle().equals(title));
        assertTrue(c.getId() == ide);
    }
    @Test
    public void testCategoryModelSetters() {
        String title = "testing title";
        int ide = 123;
        Category c = new Category();
        c.setId(ide);
        c.setTitle(title);
        assertTrue(c.getTitle().equals(title));
        assertTrue(c.getId() == ide);
    }
}
