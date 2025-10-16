package io.github.cursodsousa.icompras.pedidos.model;

import io.github.cursodsousa.icompras.pedidos.client.representation.ClientRepresentation;
import io.github.cursodsousa.icompras.pedidos.controller.dto.DadosPagamentoDto;
import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedidos;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "codigo_cliente", nullable = false)
    private Long codigoCliente;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Column(name = "chave_pagamento")
    private String chavePagamento;

    @Column(name = "observacoes")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusPedidos status;

    @Column(name = "total", nullable = false, precision = 16, scale = 2)
    private BigDecimal total;

    @Column(name = "codigo_rastreio", length = 255)
    private String codigoRastreio;

    @Column(name = "url_nf")
    private String urlNf;

    @Transient
    private DadosPagamento dadosPagamento;

    @Transient
    private ClientRepresentation dadosCliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

}