package org.riesgo;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.MultipartConfigElement;

import org.riesgo.dao.PeligroRepository;
import org.riesgo.model.Peligro;
import org.riesgo.utils.DataHelper;
import org.riesgo.utils.PeligroSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@EnableMongoRepositories
@Import(RepositoryRestMvcConfiguration.class)
@ComponentScan
@EnableAutoConfiguration
public class Application extends RepositoryRestMvcConfiguration implements CommandLineRunner {

	@Autowired
	//TODO: remove repository when test data is no longer needed 
	private PeligroRepository peligroRepository;
	private final static String ROOT_PATH = "/risk-service";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	//TODO: remove interface implementation 
	@Override
	public void run(String... args) throws Exception {
		
		peligroRepository.delete(peligroRepository.findPeligroBySource(PeligroSource.WEBPAGE));
		
		for (Peligro peligro : DataHelper.buildPeligrosFromSite()) {
			peligroRepository.save(peligro);
		}
	}

	// TODO: move this config to servlet mapping in application context 
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		super.configureRepositoryRestConfiguration(config);
		
	    try {
		      config.setBaseUri(new URI(ROOT_PATH));
		    } catch (URISyntaxException e) {
		      e.printStackTrace();
		    }
	}
	
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("6MB");
        factory.setMaxRequestSize("6MB");
        return factory.createMultipartConfig();
    }
}