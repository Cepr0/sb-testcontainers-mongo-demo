package io.github.cepr0.demo;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@With
@Document
public class Model {
    @Id private String id;
    private String text;
}
