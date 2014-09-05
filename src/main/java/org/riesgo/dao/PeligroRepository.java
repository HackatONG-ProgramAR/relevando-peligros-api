package org.riesgo.dao;

import java.util.Set;

import org.riesgo.model.Peligro;
import org.riesgo.utils.PeligroSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Operaciones CRUD basicas soportadas por MongoRepository. El resto de las
 * operaciones tiene que ser definida como los ejemplos abajo
 *
 */
@RepositoryRestResource(collectionResourceRel = "peligros", path = "peligro")
public interface PeligroRepository extends MongoRepository<Peligro, String> {

	@Description(value = "http://localhost:8080/risk-service/peligro/search/findByTitulo?titulo=peligro1")
	Set<Peligro> findPeligroByTitulo(@Param("titulo") String titulo);

	@Description(value = "http://localhost:8080/risk-service/peligro/search/findRiesgoById?id=5404c5dfb16ed0fa02617e37")
	Set<Peligro> findPeligroById(@Param("id") String id);
	
	Set<Peligro> findPeligroBySource(@Param("source") PeligroSource source);
	
	// Por datos anidados de persona
	// @Deprecated
	// @Query("{'persona.nombre' : ?0, 'persona.apellido' : ?1}")
	// Set<Peligro> findByPersona(@Param("nombre") String nombre,
	// @Param("apellido") String apellido);

	// and so on...
}
