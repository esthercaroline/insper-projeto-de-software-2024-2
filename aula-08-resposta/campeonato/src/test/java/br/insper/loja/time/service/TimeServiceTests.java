package br.insper.loja.time.service;

import br.insper.loja.time.exception.TimeNaoEncontradoException;
import br.insper.loja.time.model.Time;
import br.insper.loja.time.repository.TimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTests {

    @InjectMocks
    private TimeService timeService;

    @Mock
    private TimeRepository timeRepository;

    @Test
    public void testCadastrarTime_Sucesso() {
        Time time = new Time("Time A", "TA123", "Estadio A", "SP");

        Mockito.when(timeRepository.save(Mockito.any(Time.class))).thenReturn(time);

        Time resultado = timeService.cadastrarTime(time);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Time A", resultado.getNome());
        Mockito.verify(timeRepository, Mockito.times(1)).save(time);
    }

    @Test
    public void testCadastrarTime_DadosInvalidos() {
        Time time = new Time("", "", "Estadio A", "SP");

        Assertions.assertThrows(RuntimeException.class, () -> timeService.cadastrarTime(time));
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testListarTimesWhenEstadoIsNull() {
        Mockito.when(timeRepository.findAll()).thenReturn(new ArrayList<>());

        List<Time> times = timeService.listarTimes(null);

        Assertions.assertTrue(times.isEmpty());
    }

    @Test
    public void testListarTimesWhenEstadoIsNotNull() {
        List<Time> lista = new ArrayList<>();
        Time time = new Time();
        time.setEstado("SP");
        time.setIdentificador("time-1");
        lista.add(time);

        Mockito.when(timeRepository.findByEstado(Mockito.anyString())).thenReturn(lista);

        List<Time> times = timeService.listarTimes("SP");

        Assertions.assertTrue(times.size() == 1);
        Assertions.assertEquals("SP", times.get(0).getEstado());
        Assertions.assertEquals("time-1", times.get(0).getIdentificador());
    }

    @Test
    public void testGetTimeWhenTimeIsNotNull() {
        Time time = new Time();
        time.setEstado("SP");
        time.setIdentificador("time-1");

        Mockito.when(timeRepository.findById(1)).thenReturn(Optional.of(time));

        Time timeRetorno = timeService.getTime(1);

        Assertions.assertNotNull(timeRetorno);
        Assertions.assertEquals("SP", timeRetorno.getEstado());
        Assertions.assertEquals("time-1", timeRetorno.getIdentificador());
    }

    @Test
    public void testGetTimeWhenTimeIsNull() {
        Mockito.when(timeRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(TimeNaoEncontradoException.class,
                () -> timeService.getTime(1));
    }
}
