package io.github.cursodsousa.icompras.pedidos.controller.dto;

import java.util.List;

public record NovoPedidoDto(
        Long codigoCliente,
        DadosPagamentoDto dadosPagamento,
        List<ItemPedidoDto> itens
) {
}
