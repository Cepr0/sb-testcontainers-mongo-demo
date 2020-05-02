package io.github.cepr0.demo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModelRepoImpl implements ModelRepo {

    private final MongoTemplate mongo;

    public ModelRepoImpl(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public Model create(Model model) {
        return mongo.insert(model);
    }

    @Override
    public List<Model> getAll() {
        return mongo.findAll(Model.class);
    }
}
