package br.insper.tabela.tabela;

import br.insper.tabela.partida.PartidaService;
import br.insper.tabela.partida.RetornarPartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TabelaService {

    @Autowired
    private PartidaService partidaService;

    public List<TabelaDTO> getTabela() {

        List<RetornarPartidaDTO> partidas = partidaService.getPartidas();

        Map<String, TabelaDTO> tabela = new HashMap<>();

        for (RetornarPartidaDTO partida : partidas) {
            if (partida.getStatus().equals("REALIZADA")) {
                processarTime(tabela, partida.getNomeMandante(), partida.getPlacarMandante(), partida.getPlacarVisitante());
                processarTime(tabela, partida.getNomeVisitante(), partida.getPlacarVisitante(), partida.getPlacarMandante());
            }
        }

        return new ArrayList<>(tabela.values()).stream()
                .sorted(Comparator.comparing(TabelaDTO::getPontos).reversed())
                .collect(Collectors.toList());
    }

    private void processarTime(Map<String, TabelaDTO> tabela, String time, int golsPro, int golsContra) {
        TabelaDTO tabelaTime = tabela.getOrDefault(time, new TabelaDTO(time, 0, 0, 0));

        tabelaTime.setGolsPro(tabelaTime.getGolsPro() + golsPro);
        tabelaTime.setGolsContra(tabelaTime.getGolsContra() + golsContra);

        if (golsPro > golsContra) {
            tabelaTime.setPontos(tabelaTime.getPontos() + 3); // Vitória
        } else if (golsPro == golsContra) {
            tabelaTime.setPontos(tabelaTime.getPontos() + 1); // Empate
        }

        tabela.put(time, tabelaTime);
    }

}
