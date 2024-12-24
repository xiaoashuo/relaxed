package com.relaxed.test.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Yakir
 * @Topic YamlPropertySourceFactory
 * @Description
 * @date 2024/12/23 14:23
 * @Version 1.0
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

		String sourceName = name != null ? name : resource.getResource().getFilename();
		if (!resource.getResource().exists()) {
			return new PropertiesPropertySource(sourceName, new Properties());
		}
		else if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
			Properties propertiesFromYaml = loadYml(resource);
			return new PropertiesPropertySource(sourceName, propertiesFromYaml);
		}
		else {
			return super.createPropertySource(name, resource);
		}

	}

	private Properties loadYml(EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

}
