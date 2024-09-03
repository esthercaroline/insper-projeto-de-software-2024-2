package br.insper.loja.partida.service;

import br.insper.loja.partida.dto.EditarPartidaDTO;
import br.insper.loja.partida.dto.RetornarPartidaDTO;
import br.insper.loja.partida.dto.SalvarPartidaDTO;
import br.insper.loja.partida.exception.PartidaNaoEncontradaException;
import br.insper.loja.partida.model.Partida;
import br.insper.loja.partida.repository.PartidaRepository;
import br.insper.loja.time.model.Time;
import br.insper.loja.time.service.TimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceTests {

    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private TimeService timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarPartida_Sucesso() {
        SalvarPartidaDTO salvarPartidaDTO = new SalvarPartidaDTO();
        salvarPartidaDTO.setMandante(1);
        salvarPartidaDTO.setVisitante(2);

        Time mandante = new Time();
        mandante.setNome("Time Mandante");
        Time visitante = new Time();
        visitante.setNome("Time Visitante");

        Mockito.when(timeService.getTime(1)).thenReturn(mandante);
        Mockito.when(timeService.getTime(2)).thenReturn(visitante);

        Partida partida = new Partida();
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setStatus("AGENDADA");

        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partida);

        RetornarPartidaDTO result = partidaService.cadastrarPartida(salvarPartidaDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("AGENDADA", result.getStatus());
        Assertions.assertEquals("Time Mandante", result.getNomeMandante());
        Assertions.assertEquals("Time Visitante", result.getNomeVisitante());
    }

    @Test
    public void testListarPartidas_SemMandante() {
        List<Partida> partidas = new ArrayList<>();
        Partida partida1 = new Partida();
        Time mandante = new Time();
        mandante.setNome("Time Mandante");
        Time visitante = new Time();
        visitante.setNome("Time Visitante");
        partida1.setMandante(mandante);
        partida1.setVisitante(visitante);
        partidas.add(partida1);

        Mockito.when(partidaRepository.findAll()).thenReturn(partidas);

        List<RetornarPartidaDTO> result = partidaService.listarPartidas(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Time Mandante", result.get(0).getNomeMandante());
        Assertions.assertEquals("Time Visitante", result.get(0).getNomeVisitante());
        Mockito.verify(partidaRepository, times(1)).findAll();
    }

    @Test
    public void testListarPartidas_ComMandante() {
        List<Partida> partidas = new ArrayList<>();
        Partida partida1 = new Partida();
        Time mandante = new Time();
        mandante.setIdentificador("mandante-1");
        mandante.setNome("Time Mandante");
        Time visitante = new Time();
        visitante.setNome("Time Visitante");
        partida1.setMandante(mandante);
        partida1.setVisitante(visitante);
        partidas.add(partida1);

        Mockito.when(partidaRepository.findAll()).thenReturn(partidas);

        List<RetornarPartidaDTO> result = partidaService.listarPartidas("mandante-1");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Time Mandante", result.get(0).getNomeMandante());
        Assertions.assertEquals("Time Visitante", result.get(0).getNomeVisitante());
    }

    @Test
    public void testEditarPartida_Sucesso() {
        EditarPartidaDTO editarPartidaDTO = new EditarPartidaDTO();
        editarPartidaDTO.setPlacarMandante(2);
        editarPartidaDTO.setPlacarVisitante(3);

        Partida partida = new Partida();
        Time mandante = new Time();
        mandante.setNome("Time Mandante");
        Time visitante = new Time();
        visitante.setNome("Time Visitante");
        partida.setMandante(mandante);
        partida.setVisitante(visitante);

        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.of(partida));
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partida);

        RetornarPartidaDTO result = partidaService.editarPartida(editarPartidaDTO, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getPlacarMandante());
        Assertions.assertEquals(3, result.getPlacarVisitante());
    }

    @Test
    public void testEditarPartida_PartidaNaoEncontrada() {
        EditarPartidaDTO editarPartidaDTO = new EditarPartidaDTO();

        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> {
            partidaService.editarPartida(editarPartidaDTO, 1);
        });
    }


    @Test
    public void testGetPartida_Sucesso() {
        Partida partida = new Partida();
        Time mandante = new Time();
        mandante.setNome("Time Mandante");
        Time visitante = new Time();
        visitante.setNome("Time Visitante");
        partida.setMandante(mandante);
        partida.setVisitante(visitante);

        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.of(partida));

        RetornarPartidaDTO result = partidaService.getPartida(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Time Mandante", result.getNomeMandante());
        Assertions.assertEquals("Time Visitante", result.getNomeVisitante());
    }

    @Test
    public void testGetPartida_PartidaNaoEncontrada() {
        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> {
            partidaService.getPartida(1);
        });
    }
}
