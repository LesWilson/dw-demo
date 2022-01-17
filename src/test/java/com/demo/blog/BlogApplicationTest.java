package com.demo.blog;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class BlogApplicationTest {

    @Test
    public void testGetApplicationName() {
        BlogApplication app = new BlogApplication();
        String name = app.getName();
        assertThat(name).isEqualTo("BlogApplication");
    }

    @Test
    public void testApplicationMainMethod() {
        String[] noData = new String[]{};
        try {
            BlogApplication.main(noData);
        }
        catch (Exception e) {
            fail("Exception wasn't expected XD!");
        }
    }
}