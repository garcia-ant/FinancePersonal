package com.gposter.minhasfinancas.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.gposter.minhasfinancas.model.enums.StatusRelease;
import com.gposter.minhasfinancas.model.enums.TypeRelease;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tb_realese", schema = "financas")
@Builder
@Data
public class Release {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "mes")
	private Integer mes;

	@Column(name = "ano")
	private Integer ano;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private User usuario;

	@Column(name = "valor")
	private BigDecimal valor;

	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadrastro;

	@Column(name = "tipo")
	@Enumerated(value = EnumType.STRING)
	private TypeRelease tipo;

	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusRelease status;
	
	

}
