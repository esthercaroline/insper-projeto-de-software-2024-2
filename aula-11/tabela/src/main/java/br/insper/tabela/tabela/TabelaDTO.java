package br.insper.tabela.tabela;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabelaDTO {
    private String time;
    private Integer pontos;
    private Integer golsPro;
    private Integer golsContra;


}
