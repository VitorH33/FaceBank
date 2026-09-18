package br.com.isiflix.facebank.service.transacao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.isiflix.facebank.dto.ExtratoDTO;
import br.com.isiflix.facebank.dto.PagamentoDTO;
import br.com.isiflix.facebank.dto.TransferenciaDTO;
import br.com.isiflix.facebank.exceptions.InvalidAccountException;
import br.com.isiflix.facebank.exceptions.InvalidDateIntervalException;
import br.com.isiflix.facebank.exceptions.NotEnoughBalanceException;
import br.com.isiflix.facebank.model.Conta;
import br.com.isiflix.facebank.model.Transacao;
import br.com.isiflix.facebank.repo.ContaRepo;
import br.com.isiflix.facebank.repo.TransacaoRepo;

@Service
public class TransacaoServiceImpl implements ITransacaoService {

	
	@Autowired
	private TransacaoRepo transacaoRepo;
	
	@Autowired
	private ContaRepo contaRepo;
	
	@Override
	public boolean efetuarPagamento(PagamentoDTO pagamento) {
		Conta conta = contaRepo.findById(pagamento.numeroConta()).orElse(null);
		if (conta == null) {
			throw new InvalidAccountException("Account "+pagamento.numeroConta()+" does not exist");
		}
		if (conta.getSaldo()+ conta.getLimite() < pagamento.valor()) {
			throw new NotEnoughBalanceException("Not enough balance available for Account #"+pagamento.numeroConta());
		}
		Transacao transacao = new Transacao();
		Double saldoIni, saldoFim;
		saldoIni = conta.getSaldo();
		saldoFim = conta.getSaldo() - pagamento.valor();
		transacao.setConta(conta);
		transacao.setValor(pagamento.valor());
		transacao.setSaldoInicial(saldoIni);
		transacao.setDataHora(pagamento.dataHora());
		transacao.setDescricao(pagamento.descricao());
		transacao.setNumeroDocumento(pagamento.numDoc());
		transacao.setTipo(-1);
		transacao.setSaldoFinal(saldoFim);
		transacaoRepo.save(transacao);
		
		conta.setSaldo(saldoFim);
		contaRepo.save(conta);		
		return true;
	}

	@Override
	public boolean efetuarTransferencia(TransferenciaDTO transferencia) {
		// TODO Auto-generated method stub
		Conta contaOrigem;
		Conta contaDestino;
		
		contaOrigem = contaRepo.findById(transferencia.contaOrigem()).orElse(null);
		contaDestino = contaRepo.findById(transferencia.contaDestino()).orElse(null);
		if (contaOrigem == null || contaDestino == null) {
			throw new InvalidAccountException("Invalid Source or Destination Account");
		}
		if (contaOrigem.getSaldo() + contaOrigem.getLimite() < transferencia.valor()) {
			throw new NotEnoughBalanceException("Not Enough Balance for Account #"+contaOrigem.getNumeroConta());
		}
		
		Transacao trDebito, trCredito;
		Double saldoIniO, saldoIniD, saldoFimO, saldoFimD;
		saldoIniO = contaOrigem.getSaldo();
		saldoFimO = contaOrigem.getSaldo() - transferencia.valor();
		
		saldoIniD = contaDestino.getSaldo();
		saldoFimD = contaDestino.getSaldo() + transferencia.valor();
		
		trDebito = new Transacao();
		trDebito.setConta(contaOrigem);
		trDebito.setDataHora(transferencia.dataHora());
		trDebito.setValor(transferencia.valor());
		trDebito.setSaldoInicial(saldoIniO);
		trDebito.setSaldoFinal(saldoFimO);
		trDebito.setTipo(-1);
		trDebito.setDescricao(transferencia.descricao());
		trDebito.setNumeroDocumento(UUID.randomUUID().toString());
		
		trCredito = new Transacao();
		trCredito.setConta(contaDestino);
		trCredito.setDataHora(transferencia.dataHora());
		trCredito.setValor(transferencia.valor());
		trCredito.setSaldoInicial(saldoIniD);
		trCredito.setSaldoFinal(saldoFimD);
		trCredito.setTipo(1);
		trCredito.setDescricao(transferencia.descricao());
		trCredito.setNumeroDocumento(UUID.randomUUID().toString());
		
		transacaoRepo.save(trCredito);
		transacaoRepo.save(trDebito);
		
		contaOrigem.setSaldo(saldoFimO);
		contaDestino.setSaldo(saldoFimD);
		
		contaRepo.save(contaOrigem);
		contaRepo.save(contaDestino);
		return true;
	}

	@Override
	public List<Transacao> getExtrato(ExtratoDTO extrato) {
		Conta conta = contaRepo.findById(extrato.numeroConta()).orElse(null);
		if (conta == null) {
			throw new InvalidAccountException("Invalid Account number #"+extrato.numeroConta());
		}
		if (extrato.inicio().isAfter(extrato.fim())) {
			throw new InvalidDateIntervalException("Invalid Date Interval");
		}
		
		return transacaoRepo.findByContaAndDataHoraBetween(conta, extrato.inicio(), extrato.fim());
	}

	
}