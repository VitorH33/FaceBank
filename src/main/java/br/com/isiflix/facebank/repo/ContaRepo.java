package br.com.isiflix.facebank.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.isiflix.facebank.model.Cliente;
import br.com.isiflix.facebank.model.Conta;

public interface ContaRepo extends CrudRepository<Conta, Integer> {

	public List<Conta> findByCliente(Cliente ciente);
}