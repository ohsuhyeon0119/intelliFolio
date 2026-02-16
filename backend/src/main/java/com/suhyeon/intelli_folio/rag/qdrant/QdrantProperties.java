package com.suhyeon.intelli_folio.rag.qdrant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "qdrant")
@Getter
@Setter
public class QdrantProperties {
    private String host;
    private String collection;
    private int vectorSize;
}