package br.com.isiflix.facebank.dto;

import br.com.isiflix.facebank.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClienteDTO (@NotNull String nome, 
						  @NotNull @Email String email, 
						  @NotNull String cpf, 
						  @NotNull @Min(11) String telefone, 
						  @NotNull @Min (8) String senha){

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