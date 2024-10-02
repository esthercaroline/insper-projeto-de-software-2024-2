package br.insper.tabela.tabela;

import br.insper.tabela.partida.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TabelaService {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private TabelaRepository tabelaRepository;

    public List<TabelaDTO> getTabela() {

        return null;
        /*
        List<RetornarPartidaDTO> partidas = partidaService.getPartidas();

        Map<String, TabelaDTO> tabela = new HashMap<>();

        for (RetornarPartidaDTO partida : partidas) {
            if (partida.getStatus().equals("REALIZADA")) {
                processarTime(tabela, partida.getNomeMandante(), partida.getPlacarMandante(), partida.getPlacarVisitante());
                processarTime(tabela, partida.getNomeVisitante(), partida.getPlacarVisitante(), partida.getPlacarMandante());
            }
        }

        tabelaRepository.deleteAll();
        for (TabelaDTO tabelaDTO : tabela.values()) {
            Tabela tab = new Tabela();
            tab.setTime(tabelaDTO.getTime());
            tab.setPontos(tabelaDTO.getPontos());
            tab.setGolsContra(tabelaDTO.getGolsContra());
            tab.setGolsPro(tabelaDTO.getGolsPro());
            tabelaRepository.save(tab);
        }

        return new ArrayList<>(tabela.values()).stream()
                .sorted(Comparator.comparing(TabelaDTO::getPontos).reversed())
                .collect(Collectors.toList());

         */

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
