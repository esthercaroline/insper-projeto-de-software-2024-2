package br.insper.tabela.partida;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PartidaService {

    public List<RetornarPartidaDTO> getPartidas() {
        RestTemplate restTemplate = new RestTemplate();
        RetornarPartidaDTO[] lista = restTemplate.getForEntity(
                "http://localhost:8080/partida",
                RetornarPartidaDTO[].class).getBody();
        return Arrays.asList(lista);
    }

}
