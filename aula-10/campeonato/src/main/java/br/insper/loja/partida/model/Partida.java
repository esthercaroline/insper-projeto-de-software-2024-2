package br.insper.loja.partida.model;

import br.insper.loja.time.model.Time;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String identificador;

    @ManyToOne
    @JoinColumn(name = "id_mandante")
    private Time mandante;
    @ManyToOne
    @JoinColumn(name = "id_visitante")
    private Time visitante;
    private Integer placarMandante;

    private Integer placarVisitante;


    private String status;


}
