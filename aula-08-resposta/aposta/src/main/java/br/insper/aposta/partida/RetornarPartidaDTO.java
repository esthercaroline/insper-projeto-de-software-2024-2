package br.insper.aposta.partida;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetornarPartidaDTO {
    private String nomeMandante;
    private String nomeVisitante;
    private Integer placarMandante;
    private Integer placarVisitante;
    private String status;

    public boolean isEmpate() {
        return placarMandante == placarVisitante;
    }

    public boolean isVitoriaMandante() {
        return placarMandante > placarVisitante;
    }

    public boolean isVitoriaVisitante() {
        return placarVisitante > placarMandante;
    }

    public String getNomeMandante() {
        return nomeMandante;
    }

    public void setNomeMaFndante(String nomeMandante) {
        this.nomeMandante = nomeMandante;
    }

    public String getNomeVisitante() {
        return nomeVisitante;
    }

    public void setNomeVisitante(String nomeVisitante) {
        this.nomeVisitante = nomeVisitante;
    }

    public Integer getPlacarMandante() {
        return placarMandante;
    }

    public void setPlacarMandante(Integer placarMandante) {
        this.placarMandante = placarMandante;
    }

    public Integer getPlacarVisitante() {
        return placarVisitante;
    }

    public void setPlacarVisitante(Integer placarVisitante) {
        this.placarVisitante = placarVisitante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
