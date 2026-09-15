package br.com.isiflix.facebank.service.cliente;

import br.com.isiflix.facebank.dto.ClienteDTO;

public interface IClienteService {
	
	public Integer cadastrarCliente(ClienteDTO novo);
	public Integer alterarDados(ClienteDTO cliente);

}