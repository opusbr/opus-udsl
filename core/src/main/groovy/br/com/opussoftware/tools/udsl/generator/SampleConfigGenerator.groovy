package br.com.opussoftware.tools.udsl.generator

/**
 * <p>Optional extension to the base Generator interface that allows
 * given generator to generate a sample config file.
 * </p> 
 * 
 * 
 * <p>Generator authors are encouraged to implement this interface since
 * it makes the live better to end users as they don't have to go into docs
 * in order to find which config properties are available.
 * </p>
 * @author Philippe
 *
 */
interface SampleConfigGenerator extends Generator {
	
	void generateSampleConfig(File outputDirOrFile, ResourceLoader loader)
}
