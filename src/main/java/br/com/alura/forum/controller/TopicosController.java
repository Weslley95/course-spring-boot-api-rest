package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

/**
 * 
 * @RestControllertTodo metodo tera @ResponseBody
 * @RequestMapping responsavel por todas as requisições de /topico
 */
@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	// Injeção de dependências
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	/**
	 * Listagem de tópico
	 * TopicoForm -> API p/ Cliente
	 * 
	 * @param String nomeCurso
	 * 
	 * @return object topicos
	 */
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		
		if(nomeCurso == null) {			
			// Consulta obtendo todos os registros do BD
			List<Topico> topicos = topicoRepository.findAll();
			
			// Lista de objetos, antes de retornar converta em lista de tópico Dto
			return TopicoDto.converter(topicos);
		} else {
			// Filtrar por parametro
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			
			return TopicoDto.converter(topicos);
		}
		
	}
	
	/**
	 * Cadastro
	 * 
	 * @param form formulario
	 * @param uriBuilder requisição HTTP
	 * 
	 * TopicoForm -> Cliente p/ API
	 * Caso utilize void será devolvido HTTP 200 (resposta sem conteudo)
	 * 
	 * @return HTTP 201
	 */
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { // @RequestBody pegar informação do corpo da requisição
		
		Topico topico = form.converter(cursoRepository);
		
		topicoRepository.save(topico);
		
		// URL - Created URI
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		// Return HTTP 201, com conteudo 
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	/**
	 * Detalhamento do topico
	 * 
	 * @param Long id do topico
	 * 
	 * @return object response entity
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		
		// Verifica se existe um topico com {id} recebido
		if(topico.isPresent()) {
			new ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	/**
	 * Atualização do topico
	 * 
	 * @param id do topico
	 * @param form formulario
	 * @return object response entity
	 */
	@PutMapping("/{id}")
	@Transactional // Disparar commit (atualização no BD)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		// Verifica se existe um topico com {id} recebido
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	/**
	 * Excluir topico
	 * 
	 * @param id do topico
	 * @return HTTP 200
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		
		// Verifica se existe um topico com {id} recebido
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
