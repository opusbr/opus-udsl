package io.github.opusbr.tools.udsl.generator.test

import org.springframework.stereotype.Component

import io.github.opusbr.tools.udsl.generator.Generator
import io.github.opusbr.tools.udsl.generator.ResourceLoader
import io.github.opusbr.udsl.model.EnvironmentSpec

@Component
class DummyGenerator implements Generator {

	@Override
	public String getName() {
		return "dummy";
	}

	@Override
	public String getDescription() {
		return "Gerador dummy para testes do chassis do gerador";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "PMS";
	}

	@Override
	public int generate(List<EnvironmentSpec> envSpec, ConfigObject generatorSpecFile, File outputDir, ResourceLoader resourceLoader ) {
		return 0;
	}
}
