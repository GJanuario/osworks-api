package com.algaworks.osworks.api.controller;

import com.algaworks.osworks.api.model.ComentarioDTO;
import com.algaworks.osworks.api.model.ComentarioInput;
import com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osworks.domain.model.Comentario;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentario")
public class ComentarioController {

  @Autowired
  private GestaoOrdemServicoService gestaoOrdemServico;

  @Autowired
  private OrdemServicoRepository ordemServicoRepository;

  @Autowired
  private ModelMapper modelMapper;

  @GetMapping
  public List<ComentarioDTO> listar(@PathVariable Long ordemServicoId) {
    OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
      .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!"));

    return toCollectionDTO(ordemServico.getComentarios());

  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ComentarioDTO adicionar(@PathVariable Long ordemServicoId,
                                 @Valid @RequestBody ComentarioInput comentarioInput) {
    Comentario comentario = gestaoOrdemServico.adicionarComentario(ordemServicoId,
              comentarioInput.getDescricao());

    return toDTO(comentario);
  }

  private ComentarioDTO toDTO(Comentario comentario) {
    return modelMapper.map(comentario, ComentarioDTO.class);
  }

  private List<ComentarioDTO> toCollectionDTO(List<Comentario> comentarios) {
    return comentarios.stream()
      .map(comentario -> toDTO(comentario))
      .collect(Collectors.toList());
  }

}
