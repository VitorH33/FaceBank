package br.com.isiflix.facebank.service.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.isiflix.facebank.dto.ClienteDTO;
import br.com.isiflix.facebank.model.Cliente;
import br.com.isiflix.facebank.repo.ClienteRepo;
import jakarta.validation.Valid;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private ClienteRepo repo;
	
	@Override
	public Integer cadastrarCliente(@Valid ClienteDTO novo) {
		// TODO Auto-generated method stub
		Cliente teste = repo.findByEmailOrCpfOrTelefone(novo.email(), novo.cpf(), novo.telefone());
		if (teste != null)
			return null;
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Cliente cliente = novo.toCliente();
		String novaSenha = encoder.encode(cliente.getSenha());
		cliente.setSenha(novaSenha);
		
		return repo.save(cliente).getIdCliente();
	}

	@Override
	public Integer alterarDados(ClienteDTO cliente) {
		// TODO Auto-generated method stub
		return repo.save(cliente.toCliente()).getIdCliente();
	}

}