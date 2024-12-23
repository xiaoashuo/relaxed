package com.relaxed.test.factory;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic YamlPropertySourceFactory
 * @Description
 * @date 2024/12/23 14:23
 * @Version 1.0
 */
public class YamlPropertySourceFactory  implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String resourceName = Optional.ofNullable(name).orElse(resource.getResource().getFilename());
        List<PropertySource<?>> yamlSources = new YamlPropertySourceLoader().load(resourceName, resource.getResource());
        return yamlSources.get(0);
    }

}
