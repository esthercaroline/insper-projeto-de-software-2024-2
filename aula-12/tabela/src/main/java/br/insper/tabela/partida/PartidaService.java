package br.insper.tabela.partida;

import br.insper.loja.partida.dto.RetornarPartidaDTO;
import br.insper.tabela.tabela.Tabela;
import br.insper.tabela.tabela.TabelaDTO;
import br.insper.tabela.tabela.TabelaRepository;
import br.insper.tabela.tabela.TabelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    @Autowired
    private TabelaRepository tabelaRepository;

    @KafkaListener(topics = "partidas")
    public void getPartidas(RetornarPartidaDTO dto) {

        List<Tabela> tabelas = tabelaRepository.findAll();

        for (Tabela tabela : tabelas) {
            if (tabela.getTime().equals(dto.getNomeMandante())) {

            } else if (tabela.getTime().equals(dto.getNomeVisitante())) {

            }


        }


    }


}
