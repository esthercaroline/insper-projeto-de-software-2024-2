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
    public void testListarTimesWhenEstadoIsNull() {

        // preparacao
        Mockito.when(timeRepository.findAll()).thenReturn(new ArrayList<>());

        // chamada do codigo testado
        List<Time> times = timeService.listarTimes(null);

        // verificacao dos resultados
        Assertions.assertTrue(times.isEmpty());
    }

    @Test
    public void testListarTimesWhenEstadoIsNotNull() {

        // preparacao
        List<Time> lista = new ArrayList<>();

        Time time = new Time();
        time.setEstado("SP");
        time.setIdentificador("time-1");
        lista.add(time);

        Mockito.when(timeRepository.findByEstado(Mockito.anyString())).thenReturn(lista);

        // chamada do codigo testado
        List<Time> times = timeService.listarTimes("SP");

        // verificacao dos resultados
        Assertions.assertTrue(times.size() == 1);
        Assertions.assertEquals("SP", times.getFirst().getEstado());
        Assertions.assertEquals("time-1", times.getFirst().getIdentificador());
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
