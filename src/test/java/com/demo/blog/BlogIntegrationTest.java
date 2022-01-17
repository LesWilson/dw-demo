package com.demo.blog;

import com.demo.blog.api.Representation;
import com.demo.blog.config.BlogConfiguration;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import io.dropwizard.testing.ResourceHelpers;
import javax.ws.rs.client.Entity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BlogIntegrationTest {

    public static DropwizardAppExtension<BlogConfiguration> app = new DropwizardAppExtension<>(
            BlogApplication.class,
            ResourceHelpers.resourceFilePath("config.yml")
    );

    @Test
    public void testGetRequestOnBaseURI() {
        Client client = app.client();
        Response response = client.target(
                String.format("http://localhost:%d", app.getLocalPort()))
                .request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        Map data = response.readEntity(Map.class);
        assertThat((data.get("content"))).isEqualTo("Hello World!");
    }

    @Test
    public void testGetRequestOnHeyURI() {
        Client client = app.client();
        Response response = client.target(
                String.format("http://localhost:%d/hey", app.getLocalPort()))
                .request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        Map data = response.readEntity(Map.class);
        assertThat((data.get("content"))).isEqualTo("Hello World!");
    }

    @Test
    public void testPostRequestOnHeyURI() {
        Client client = app.client();
        Representation<String> requestPOJO = new Representation<>(123, "New Content");
        Response response = client.target(
                String.format("http://localhost:%d/hey", app.getLocalPort()))
                .request().post(Entity.json(requestPOJO));
        assertThat(response.getStatus()).isEqualTo(200);
        Representation<String> actual = response.readEntity(Representation.class);
        assertThat(actual.getData()).isEqualTo(requestPOJO.getData());
    }

}