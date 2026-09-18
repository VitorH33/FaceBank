package br.com.isiflix.facebank.dto;

import java.time.LocalDateTime;

public record ExtratoDTO(Integer numeroConta, LocalDateTime inicio, LocalDateTime fim) {

}