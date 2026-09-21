package br.com.isiflix.facebank.dto;

import org.hibernate.validator.constraints.Length;

import br.com.isiflix.facebank.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ClienteDTO (@NotNull String nome, 
		                  @NotNull @Email String email, 
		                  @NotNull String cpf, 
		                  @NotNull @Length(min = 11) String telefone, 
		                  @NotNull @Length(min = 8) String senha){

	public Cliente toCliente() {
		Cliente cliente = new Cliente();
		cliente.setCpf(cpf);
		cliente.setNome(nome);
		cliente.setEmail(email);
		cliente.setTelefone(telefone);
		cliente.setSenha(senha);
		return cliente;
	}
}