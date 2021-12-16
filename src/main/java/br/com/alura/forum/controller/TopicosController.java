package br.com.alura.forum.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

/**
 * 
 * @RestControllertTodo metodo tera @ResponseBody
 *
 */
@RestController
public class TopicosController {
	
	// Injeção de dependências
	@Autowired
	private TopicoRepository topicoRepository;
	
	/**
	 * Criar topico inicial
	 * 
	 * @return
	 */
	@RequestMapping("/topicos")
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
}
