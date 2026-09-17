package br.com.isiflix.facebank.repo;

import org.springframework.data.repository.CrudRepository;

import br.com.isiflix.facebank.model.Cliente;

public interface ClienteRepo extends CrudRepository<Cliente, Integer>{
	
	public Cliente findByEmailOrCpfOrTelefone(String email, String cpf, String telefone);

}