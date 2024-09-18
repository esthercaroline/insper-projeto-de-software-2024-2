package br.insper.tabela.tabela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TabelaController {

    @Autowired
    private TabelaService tabelaService;

    @GetMapping("/tabela")
    public List<TabelaDTO> getTabela() {
        return tabelaService.getTabela();
    }
}
