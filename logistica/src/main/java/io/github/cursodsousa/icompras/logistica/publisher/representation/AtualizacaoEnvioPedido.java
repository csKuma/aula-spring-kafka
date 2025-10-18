package io.github.cursodsousa.icompras.logistica.publisher.representation;

import io.github.cursodsousa.icompras.logistica.model.StatusPedido;

public record AtualizacaoEnvioPedido(
        Long codigo,
        StatusPedido status,
        String urlNotaFiscal,
        String rastreio
) {
}
