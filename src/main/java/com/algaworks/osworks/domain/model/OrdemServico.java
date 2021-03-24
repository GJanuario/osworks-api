package com.algaworks.osworks.domain.model;

import com.algaworks.osworks.domain.exception.NegocioException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrdemServico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Cliente cliente;
  private String descricao;
  private BigDecimal preco;

  @Enumerated(EnumType.STRING)
  private StatusOrdemServico status;
  private OffsetDateTime dataAbertura;
  private OffsetDateTime dataFinalizacao;

  @OneToMany(mappedBy = "ordemServico")
  private List<Comentario> comentarios = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getPreco() {
    return preco;
  }

  public void setPreco(BigDecimal preco) {
    this.preco = preco;
  }

  public StatusOrdemServico getStatus() {
    return status;
  }

  public void setStatus(StatusOrdemServico status) {
    this.status = status;
  }

  public OffsetDateTime getDataAbertura() {
    return dataAbertura;
  }

  public void setDataAbertura(OffsetDateTime dataAbertura) {
    this.dataAbertura = dataAbertura;
  }

  public OffsetDateTime getDataFinalizacao() {
    return dataFinalizacao;
  }

  public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
    this.dataFinalizacao = dataFinalizacao;
  }

  public List<Comentario> getComentarios() {
    return comentarios;
  }

  public void setComentarios(List<Comentario> comentarios) {
    this.comentarios = comentarios;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrdemServico that = (OrdemServico) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public boolean podeSerFinalizada() {
    return StatusOrdemServico.ABERTA.equals(getStatus());
  }

  public boolean naopodeSerFinalizada() {
    return !podeSerFinalizada();
  }

  public void finalizar() {
    if(naopodeSerFinalizada()) {
      throw new NegocioException("Ordem de serviço não pode ser finalizada!");
    }
    setStatus(StatusOrdemServico.FINALIZADA);
    setDataFinalizacao(OffsetDateTime.now());
  }
}
