package br.com.isiflix.facebank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isiflix.facebank.dto.PagamentoDTO;
import br.com.isiflix.facebank.dto.ResponseDTO;
import br.com.isiflix.facebank.service.transacao.ITransacaoService;

@RestController
@CrossOrigin("*")
public class TransacaoController {

	@Autowired
	private ITransacaoService service;
	
	@PostMapping("/pagamentos")
	public ResponseEntity<ResponseDTO> efetuarPagamento(@RequestBody PagamentoDTO pagamento){
		try {
			if (service.efetuarPagamento(pagamento)) {
				return ResponseEntity.ok(new ResponseDTO("Pagamento efetuado"));
			}
			return ResponseEntity.badRequest().body(new ResponseDTO("Nao foi possivel realizar operacao"));
		}
		catch(Exception ex) {
			return ResponseEntity.badRequest().body(new ResponseDTO(ex.getMessage()));
		}
	}
}