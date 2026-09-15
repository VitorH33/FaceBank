package br.com.isiflix.facebank.cliente.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.isiflix.facebank.dto.ClienteDTO;
import br.com.isiflix.facebank.service.cliente.IClienteService;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTests {

    private ClienteDTO reqValida;
    private ClienteDTO reqInvalida;
    private Integer    idValido;

    @Mock
    private IClienteService service;

    @BeforeEach
    public void setup() {
  
        reqValida   = new ClienteDTO("Cliente Valido", "cliente@email.com","01234567890", "11852741963", "abc12345");
        reqInvalida = new ClienteDTO("Cliente Invalido", null, null, null, null);
        idValido    = 1;
    }

    @Test
    public void deveCadastrarClienteValido() {
        when(service.cadastrarCliente(reqValida)).thenReturn(idValido);

        Integer resultado = service.cadastrarCliente(reqValida);
        assertEquals(idValido, resultado);
    }

    @Test
    public void deveRejeitarClienteInvalido() {
        when(service.cadastrarCliente(reqInvalida))
                .thenThrow(new ConstraintViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> {
            service.cadastrarCliente(reqInvalida);
        });
    }
}