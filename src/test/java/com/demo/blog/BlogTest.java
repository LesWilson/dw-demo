package com.demo.blog;

import com.demo.blog.model.Part;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.blog.api.Representation;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class BlogTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testSerialization() throws Exception {
        final Representation<Part> message = new Representation<>(200, new Part(1, "Part 1", "PART_1_CODE"));
        String actual = MAPPER.writeValueAsString(message);
        String expected = fixture("fixtures/blog.json");
        System.out.println("actual:"+actual);
        System.out.println("expected:"+expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testDeserialization() throws Exception {
        final Representation<Part> expected = new Representation<>(200, new Part(1, "Part 1", "PART_1_CODE"));
        Representation<Part> actual = MAPPER.readValue(fixture("fixtures/blog.json"), Representation.class);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}