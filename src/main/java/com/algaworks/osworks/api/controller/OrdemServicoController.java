package com.algaworks.osworks.api.controller;

import com.algaworks.osworks.api.model.OrdemServicoDTO;
import com.algaworks.osworks.api.model.OrdemServicoInput;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.BeanProperty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

  @Autowired
  private GestaoOrdemServicoService gestaoOrdemServico;

  @Autowired
  private OrdemServicoRepository ordemServicoRepository;

  @Autowired
  private ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrdemServicoDTO criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
    OrdemServico ordemServico = toEntity(ordemServicoInput);
    return toDTO(gestaoOrdemServico.criar(ordemServico));
  }

  @GetMapping
  public List<OrdemServicoDTO> listar() {
    return toCollectionDTO(ordemServicoRepository.findAll());
  }

  @PutMapping("/{ordemServicoId}/finalizacao")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void finalizar(@PathVariable Long ordemServicoId) {
    gestaoOrdemServico.finalizar(ordemServicoId);
  }

  @GetMapping("/{ordemServicoId}")
  public ResponseEntity<OrdemServicoDTO> buscar(@PathVariable Long ordemServicoId) {
    Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemServicoId);
    if(ordemServico.isPresent()) {
      OrdemServicoDTO ordemServicoDTO = modelMapper.map(ordemServico.get(), OrdemServicoDTO.class);
      return ResponseEntity.ok(ordemServicoDTO);
    }

    return ResponseEntity.notFound().build();
  }

  private OrdemServicoDTO toDTO(OrdemServico ordemServico) {
    return modelMapper.map(ordemServico, OrdemServicoDTO.class);
  }

  private List<OrdemServicoDTO> toCollectionDTO(List<OrdemServico> ordemServicos) {
    return ordemServicos.stream()
      .map(ordemServico -> toDTO(ordemServico))
      .collect(Collectors.toList());
  }

  private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
    return modelMapper.map(ordemServicoInput, OrdemServico.class);
  }

}
