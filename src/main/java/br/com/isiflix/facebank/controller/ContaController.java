package br.com.isiflix.facebank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isiflix.facebank.dto.ContaDTO;
import br.com.isiflix.facebank.dto.ResponseDTO;
import br.com.isiflix.facebank.model.Conta;
import br.com.isiflix.facebank.service.conta.IContaService;

@RestController
@CrossOrigin("*")
public class ContaController {
	
	@Autowired
	private IContaService service;
	
	@PostMapping("/contas")
	public ResponseEntity<ResponseDTO> cadastrarConta(@RequestBody ContaDTO conta){
		Integer numConta = service.cadastrarNovaConta(conta);
		if (numConta != null) {
			return ResponseEntity.status(201).body(new ResponseDTO("Conta "+numConta+" criada com sucesso"));
		}
		return ResponseEntity.badRequest().body(new ResponseDTO("Dados invalidos para conta"));
	}

	@GetMapping("/contas/{idConta}")
	public ResponseEntity<Conta> recuperarContasPeloCliente(@PathVariable Integer idConta){
		Conta res = service.recuperarPeloNumero(idConta);
		if (res != null) { 
			return ResponseEntity.ok(service.recuperarPeloNumero(idConta));
		}
		return ResponseEntity.notFound().build();
	}
}