package com.demo.blog;

import com.demo.blog.config.BlogConfiguration;
import com.demo.blog.health.BlogHealthCheck;
import com.demo.blog.model.Part;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.migrations.MigrationsBundle;

import javax.sql.DataSource;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import com.demo.blog.auth.BlogAuthenticator;
import com.demo.blog.auth.BlogAuthorizer;
import com.demo.blog.auth.User;
import com.demo.blog.resource.PartsResource;
import com.demo.blog.service.PartsService;

public class BlogApplication extends Application<BlogConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    private final HibernateBundle<BlogConfiguration> hibernateBundle =
            new HibernateBundle<>(Part.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(String[] args) throws Exception {
        new BlogApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BlogConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BlogConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(BlogConfiguration configuration, Environment environment) {
        // Datasource configuration
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        BlogHealthCheck healthCheck =
                new BlogHealthCheck(dbi.onDemand(PartsService.class));
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);

        // Register OAuth authentication
        environment.jersey()
                .register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new BlogAuthenticator())
                        .setAuthorizer(new BlogAuthorizer()).setPrefix(BEARER).buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Register resources
        environment.jersey().register(new PartsResource(dbi.onDemand(PartsService.class)));
    }
}