package br.com.isiflix.facebank.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.isiflix.facebank.model.Conta;
import br.com.isiflix.facebank.model.Transacao;

public interface TransacaoRepo extends CrudRepository<Transacao, Long> {
	public List<Transacao> findByContaAndDataHoraBetween(Conta conta, LocalDateTime inicio, LocalDateTime fim);
}