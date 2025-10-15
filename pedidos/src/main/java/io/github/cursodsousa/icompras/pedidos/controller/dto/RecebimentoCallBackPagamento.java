package io.github.cursodsousa.icompras.pedidos.controller.dto;

public record RecebimentoCallBackPagamento(
        Long codigo,
        String chavePagamento,
        boolean status,
        String observacoes
) {
}
