package br.com.isiflix.facebank.service.conta;

import java.util.List;

import br.com.isiflix.facebank.dto.ContaDTO;
import br.com.isiflix.facebank.model.Cliente;
import br.com.isiflix.facebank.model.Conta;

public interface IContaService {
	public Integer cadastrarNovaConta(ContaDTO nova);
	public List<Conta> recuperarPeloCliente(Cliente cliente);
	public Conta recuperarPeloNumero(Integer numeroConta);
}