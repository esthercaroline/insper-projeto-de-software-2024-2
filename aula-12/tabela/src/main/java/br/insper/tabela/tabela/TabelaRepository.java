package br.insper.tabela.tabela;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TabelaRepository extends MongoRepository<Tabela, String> {
}
