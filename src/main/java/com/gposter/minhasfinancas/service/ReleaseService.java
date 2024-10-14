package com.gposter.minhasfinancas.service;

import java.util.List;
import java.util.Optional;

import com.gposter.minhasfinancas.model.entities.Release;
import com.gposter.minhasfinancas.model.enums.StatusRelease;

public interface ReleaseService {
	
	Release save(Release release) ;
	
	Release update(Release release);
	
	void delete(Release release);
	
	List<Release> search( Release releaseFiler);
	
	void UpdateStatus(Release release, StatusRelease status);
	void validate(Release release);
	
	Optional<Release> obterPorId(Long id);
}
