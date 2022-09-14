package br.com.elo.challenge.planetexplorer.repository;

import br.com.elo.challenge.planetexplorer.models.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanetRepository extends MongoRepository<Planet, String> {
    Planet findPlanetBySlug(String slug);
}
