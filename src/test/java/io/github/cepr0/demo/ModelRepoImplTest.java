package io.github.cepr0.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@ExtendWith(SpringExtension.class)
@DataMongoTest
@ContextConfiguration(initializers = ModelRepoImplTest.Initializer.class)
@Import(ModelRepoImpl.class)
class ModelRepoImplTest {

    @Container
    private static final GenericContainer CONTAINER = new GenericContainer("mongo:4.2").withExposedPorts(27017);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            String host = CONTAINER.getContainerIpAddress();
            Integer port = CONTAINER.getFirstMappedPort();
            String mongoUrl = String.format("spring.data.mongodb.uri=mongodb://%s:%s/test-db", host, port);
            log.info("[i] Mongo URI: {}", mongoUrl);
            TestPropertyValues values = TestPropertyValues.of(mongoUrl);
            values.applyTo(context);
        }
    }

    @Autowired private ModelRepo repo;
    @Autowired private MongoTemplate mongo;

    @Test
    void create() {
        Model model = repo.create(new Model(null, "text"));
        assertThat(mongo.findAll(Model.class).get(0)).isEqualTo(model);
    }

    @Test
    void getAll() {
        mongo.insertAll(List.of(new Model(null, "text1"), new Model(null, "text2")));
        List<Model> models = repo.getAll();
        assertThat(models).hasSize(2);
    }
}