package com.gposter.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gposter.minhasfinancas.api.dto.ReleaseDTO;
import com.gposter.minhasfinancas.exception.RuleBusinessException;
import com.gposter.minhasfinancas.model.entities.Release;
import com.gposter.minhasfinancas.model.entities.User;
import com.gposter.minhasfinancas.model.enums.StatusRelease;
import com.gposter.minhasfinancas.model.enums.TypeRelease;
import com.gposter.minhasfinancas.service.ReleaseService;
import com.gposter.minhasfinancas.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/lancamentos")
@RequiredArgsConstructor
public class ReleaseResource {

	private final ReleaseService service;
	private final UserService userService;

	@GetMapping
	public ResponseEntity buscar(
	        @RequestParam(value = "descricao", required = false) String descricao,
	        @RequestParam(value = "mes", required = false) Integer mes,
	        @RequestParam(value = "ano", required = false) Integer ano,
	        @RequestParam("usuario") Long idUsuario) {

	    Release releaseFiltro = new Release();
	    releaseFiltro.setDescricao(descricao);
	    releaseFiltro.setMes(mes);
	    releaseFiltro.setAno(ano);

	    // Busca o usuário pelo id
	    Optional<User> usuario = userService.obterPorId(idUsuario);
	    if (!usuario.isPresent()) {
	        return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
	    }
	    releaseFiltro.setUsuario(usuario.get());

	    // Busca lançamentos com base nos critérios
	    List<Release> releases = service.search(releaseFiltro);

	    return ResponseEntity.ok(releases);
	}


	@PostMapping
	public ResponseEntity save(@RequestBody ReleaseDTO dto) {
		try {
			Release entidade = Converter(dto);
			entidade = service.save(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RuleBusinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PutMapping("{id}")
	public ResponseEntity update(@PathVariable("id") Long id, @RequestBody ReleaseDTO dto) {

		return service.obterPorId(id).map(Entity -> {

			Release release = Converter(dto);
			release.setId(id);
			service.update(release);

			return ResponseEntity.ok(release);

		}).orElseGet(() -> new ResponseEntity("Lacamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {

		return service.obterPorId(id).map(entidade -> {

			service.delete(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);

		}).orElseGet(() ->

		new ResponseEntity("Lancamento  nao encontrado na base de dados ", HttpStatus.BAD_REQUEST));

	}
	private Release Converter(ReleaseDTO dto) {
	    Release release = new Release();

	    release.setId(dto.getId());
	    release.setDescricao(dto.getDescricao());
	    release.setMes(dto.getMes());
	    release.setAno(dto.getAno());
	    release.setValor(dto.getValor());

	    // Validação do tipo
	    if (dto.getTipo() == null || (!dto.getTipo().equals("DESPESA") && !dto.getTipo().equals("RECEITA"))) {
	        throw new RuleBusinessException("Tipo de lançamento inválido. Os valores permitidos são DESPESA ou RECEITA.");
	    }
	    release.setTipo(TypeRelease.valueOf(dto.getTipo()));

	    // Definir o status, se não fornecido, definir como PENDENTE por padrão
	    release.setStatus(dto.getStatus() != null ? StatusRelease.valueOf(dto.getStatus()) : StatusRelease.PENDENTE);

	    // Associar o usuário, caso o ID seja válido
	    User usuario = userService.obterPorId(dto.getUsuario())
	            .orElseThrow(() -> new RuleBusinessException("Usuário não encontrado para o ID fornecido."));
	    release.setUsuario(usuario);

	    return release;
	}

}
