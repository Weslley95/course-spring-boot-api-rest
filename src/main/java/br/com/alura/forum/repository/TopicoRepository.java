package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
	/**
	 * Consulta via SQL / assinatura do método
	 * Curso -> Relacionamento / Nome -> Atributo de Curso
	 * Relacionamento_atributo -> "_" -> Resolve Problema de ambiguidade
	 */
	List<Topico> findByCurso_Nome(String nomeCurso);

}
