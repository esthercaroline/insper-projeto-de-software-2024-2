package br.insper.aposta.aposta;

import br.insper.aposta.partida.PartidaNaoEncontradaException;
import br.insper.aposta.partida.PartidaNaoRealizadaException;
import br.insper.aposta.partida.PartidaService;
import br.insper.aposta.partida.RetornarPartidaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApostaServiceTests {

    @InjectMocks
    ApostaService apostaService;

    @Mock
    ApostaRepository apostaRepository;

    @Mock
    PartidaService partidaService;

    @Test
    public void testGetApostaWhenApostaIsNull() {
        Mockito.when(apostaRepository.findById("1"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ApostaNaoEncontradaException.class,
                () -> apostaService.getAposta("1"));
    }

    @Test
    public void testGetApostaWhenApostaIsNotNullStatusRealizada() {
        Aposta aposta = new Aposta();
        aposta.setStatus("GANHOU");

        Mockito.when(apostaRepository.findById("1"))
                .thenReturn(Optional.of(aposta));

        Aposta apostaRetorno = apostaService.getAposta("1");
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testSalvar_Sucesso() {
        Aposta aposta = new Aposta();
        aposta.setIdPartida(1);
        aposta.setResultado("EMPATE");

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("AGENDADA");

        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));
        Mockito.when(apostaRepository.save(any(Aposta.class))).thenReturn(aposta);

        Aposta result = apostaService.salvar(aposta);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("REALIZADA", result.getStatus());
        verify(apostaRepository, times(1)).save(aposta);
    }

    @Test
    public void testSalvar_PartidaNaoEncontrada() {
        Aposta aposta = new Aposta();
        aposta.setIdPartida(1);
        aposta.setResultado("EMPATE");

        // Simulando uma resposta com erro (404 NOT FOUND)
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Espera-se que uma PartidaNaoEncontradaException seja lançada
        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> apostaService.salvar(aposta));
    }

    @Test
    public void testListar() {
        Aposta aposta1 = new Aposta();
        Aposta aposta2 = new Aposta();

        Mockito.when(apostaRepository.findAll()).thenReturn(Arrays.asList(aposta1, aposta2));

        List<Aposta> result = apostaService.listar();

        Assertions.assertEquals(2, result.size());
        verify(apostaRepository, times(1)).findAll();
    }

    @Test
    public void testGetAposta_PartidaNaoRealizada() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setStatus("REALIZADA");

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("AGENDADA");  // Partida não realizada

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));

        Assertions.assertThrows(PartidaNaoRealizadaException.class,
                () -> apostaService.getAposta("1"));
    }

    @Test
    public void testGetAposta_PartidaRealizada_Empate() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setResultado("EMPATE");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("REALIZADA");
        partidaDTO.setPlacarMandante(1);
        partidaDTO.setPlacarVisitante(1);

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta); // Mock save to return aposta

        Aposta result = apostaService.getAposta("1");

        Assertions.assertNotNull(result); // Certifique-se de que o result não seja null
        Assertions.assertEquals("GANHOU", result.getStatus());
        verify(apostaRepository, times(1)).save(aposta);
    }

    @Test
    public void testGetAposta_PartidaRealizada_Perda() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setResultado("VITORIA_MANDANTE");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("REALIZADA");
        partidaDTO.setPlacarMandante(0);  // Placar que não coincide com a aposta
        partidaDTO.setPlacarVisitante(1);

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta); // Mock save to return aposta

        Aposta result = apostaService.getAposta("1");

        Assertions.assertNotNull(result); // Certifique-se de que o result não seja null
        Assertions.assertEquals("PERDEU", result.getStatus());
        verify(apostaRepository, times(1)).save(aposta);
    }

    @Test
    public void testSalvar_PartidaComErro() {
        Aposta aposta = new Aposta();
        aposta.setIdPartida(1);
        aposta.setResultado("EMPATE");

        // Simulando uma resposta com erro (404 NOT FOUND)
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Espera-se que uma PartidaNaoEncontradaException seja lançada
        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> apostaService.salvar(aposta));
    }

    @Test
    public void testGetAposta_PartidaRealizada_VitoriaMandante() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setResultado("VITORIA_MANDANTE");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("REALIZADA");
        partidaDTO.setPlacarMandante(2); // Mandante ganhou
        partidaDTO.setPlacarVisitante(1);

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));
        Mockito.when(apostaRepository.save(any(Aposta.class))).thenReturn(aposta); // Mock save to return aposta

        Aposta result = apostaService.getAposta("1");

        Assertions.assertNotNull(result); // Certifique-se de que o resultado não é null
        Assertions.assertEquals("GANHOU", result.getStatus());
        verify(apostaRepository, times(1)).save(aposta);
    }

    @Test
    public void testGetAposta_PartidaRealizada_VitoriaVisitante() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setResultado("VITORIA_VISITANTE");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("REALIZADA");
        partidaDTO.setPlacarMandante(1);
        partidaDTO.setPlacarVisitante(2); // Visitante ganhou

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));
        Mockito.when(apostaRepository.save(any(Aposta.class))).thenReturn(aposta); // Mock save to return aposta

        Aposta result = apostaService.getAposta("1");

        Assertions.assertNotNull(result); // Certifique-se de que o resultado não é null
        Assertions.assertEquals("GANHOU", result.getStatus());
        verify(apostaRepository, times(1)).save(aposta);
    }


    @Test
    public void testListarApostasQuandoNenhumaAposta() {
        Mockito.when(apostaRepository.findAll()).thenReturn(Arrays.asList());

        List<Aposta> result = apostaService.listar();

        Assertions.assertTrue(result.isEmpty());
        verify(apostaRepository, times(1)).findAll();
    }
    @Test
    public void testGetAposta_PartidaNaoEncontrada() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        // Simulando uma resposta com erro (404 NOT FOUND)
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> apostaService.getAposta("1"));
    }

    @Test
    public void testSalvar_ApostaComCamposNulos() {
        Aposta aposta = new Aposta();
        // Não definindo idPartida e resultado para simular campos nulos
        aposta.setIdPartida(null);
        aposta.setResultado(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> apostaService.salvar(aposta));
    }

    @Test
    public void testGetAposta_StatusPartidaCancelada() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("CANCELADA");

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));

        Assertions.assertThrows(PartidaNaoRealizadaException.class, () -> apostaService.getAposta("1"));
    }

    @Test
    public void testGetAposta_PartidaNaoRealizadaComErro() {
        Aposta aposta = new Aposta();
        aposta.setId("1");
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);

        RetornarPartidaDTO partidaDTO = new RetornarPartidaDTO();
        partidaDTO.setStatus("EM_ANDAMENTO");

        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(aposta.getIdPartida()))
                .thenReturn(new ResponseEntity<>(partidaDTO, HttpStatus.OK));

        Assertions.assertThrows(PartidaNaoRealizadaException.class, () -> apostaService.getAposta("1"));
    }

    @Test
    public void testGetAposta_EntradaNula() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> apostaService.getAposta(null));
    }

    @Test
    public void testSalvar_ApostaNula() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> apostaService.salvar(null));
    }

}
