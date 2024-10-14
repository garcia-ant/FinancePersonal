package com.gposter.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gposter.minhasfinancas.exception.RuleBusinessException;
import com.gposter.minhasfinancas.model.entities.Release;
import com.gposter.minhasfinancas.model.enums.StatusRelease;
import com.gposter.minhasfinancas.model.repositories.ReleaseRepository;
import com.gposter.minhasfinancas.service.ReleaseService;

@Service
public class ReleaseServiceImp implements ReleaseService {

	private final ReleaseRepository repository;

	public ReleaseServiceImp(ReleaseRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Release save(Release release) {
		validate(release);
		release.setStatus(StatusRelease.PENDENTE);
		return repository.save(release);
	}

	@Override
	@Transactional
	public Release update(Release release) {
	    // Garante que o ID do lançamento não seja nulo
	    Objects.requireNonNull(release.getId(), "O ID do lançamento não pode ser nulo");

	    // Salva a entidade no repositório
	    return repository.save(release);
	}

	@Override
	@Transactional
	public void delete(Release release) {
		Objects.requireNonNull(release.getId());
		repository.delete(release);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Release> search(Release releaseFilter) {
		Example<Release> example = Example.of(releaseFilter,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void validate(Release release) {

		if (release.getDescricao() == null || release.getDescricao().trim().equals("")) {
			throw new RuleBusinessException("Informe uma descrição valida.");
		}
		if (release.getMes() == null || release.getMes() < 1 || release.getMes() > 12) {
			throw new RuleBusinessException("Informe um Mês válido");
		}
		if (release.getAno() == null || release.getAno().toString().length() != 4) {
			throw new RuleBusinessException("Informe um Ano Válido.");
		}
		if (release.getUsuario() == null || release.getUsuario().getId() == null) {
			throw new RuleBusinessException("Informe um Usuário.");
		}

		if (release.getValor() == null || release.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RuleBusinessException("Informe um valor");
		}
		if(release.getTipo() ==null ) {
			throw new RuleBusinessException("Informe um tipo de lançamento.");
		}
	}

	@Override
	public void UpdateStatus(Release release, StatusRelease status) {
		release.setStatus(status);
		update(release);

	}

	@Override
	public Optional<Release> obterPorId(Long id) {
		return repository.findById(id);
	}
}
