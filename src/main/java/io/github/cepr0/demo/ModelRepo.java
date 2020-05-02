package io.github.cepr0.demo;

import java.util.List;

public interface ModelRepo {

    Model create(Model model);
    List<Model> getAll();
}
