package br.com.isiflix.facebank.service.conta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.isiflix.facebank.dto.ContaDTO;
import br.com.isiflix.facebank.model.Cliente;
import br.com.isiflix.facebank.model.Conta;
import br.com.isiflix.facebank.repo.ClienteRepo;
import br.com.isiflix.facebank.repo.ContaRepo;

@Service
public class ContaServiceImpl implements IContaService{

	private Integer numeroBanco = 12345;
	
	@Autowired
	private ContaRepo contaRepo;
	
	@Autowired
	private ClienteRepo cliRepo;
	@Override
	public Integer cadastrarNovaConta(ContaDTO nova) {
		// TODO Auto-generated method stub
		
		Cliente cli = cliRepo.findById(nova.idCliente()).orElse(null);
		if (cli == null) {
			return null;
		}		
		Conta conta = nova.toConta();
		conta.setNumeroBanco(numeroBanco);
		return contaRepo.save(conta).getNumeroConta();
	}

	@Override
	public List<Conta> recuperarPeloCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		return contaRepo.findByCliente(cliente);
	}

	@Override
	public Conta recuperarPeloNumero(Integer numeroConta) {
		// TODO Auto-generated method stub
		return contaRepo.findById(numeroConta).orElse(null);
	}

}