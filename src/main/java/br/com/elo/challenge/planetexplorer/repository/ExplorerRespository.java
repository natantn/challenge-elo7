package br.com.elo.challenge.planetexplorer.repository;

import br.com.elo.challenge.planetexplorer.models.Explorer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExplorerRespository extends MongoRepository<Explorer, String> {

    Explorer findExplorerBySlug(String slug);

}
