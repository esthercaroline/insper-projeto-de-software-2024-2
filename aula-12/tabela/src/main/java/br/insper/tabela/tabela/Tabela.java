package br.insper.tabela.tabela;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Tabela {
    private String id;
    private String time;
    private Integer pontos;
    private Integer golsPro;
    private Integer golsContra;
}
