package br.com.isiflix.facebank.service.transacao;

import java.util.List;

import br.com.isiflix.facebank.dto.ExtratoDTO;
import br.com.isiflix.facebank.dto.PagamentoDTO;
import br.com.isiflix.facebank.dto.TransferenciaDTO;
import br.com.isiflix.facebank.model.Transacao;


public interface ITransacaoService {

	public boolean efetuarPagamento(PagamentoDTO pagamento);
	public boolean efetuarTransferencia(TransferenciaDTO transferencia);
	public List<Transacao> getExtrato(ExtratoDTO extrato);
}