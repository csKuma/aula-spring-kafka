package io.github.cursodsousa.icompras.pedidos.publisher.representation;

import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedidos;

import java.math.BigDecimal;
import java.util.List;

public record DetalhePedidoRepresentation(
        Long codigo,
        Long codigoCliente,
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String bairro,
        String email,
        String telefone,
        String dataPedido,
        BigDecimal total,
        StatusPedidos status,
        String urlNotaFiscal,
        String rastreio,
        List<DetalheItemProdutoRepresentation> itens
) {
}
