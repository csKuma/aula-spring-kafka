package io.github.cursodsousa.icompras.faturamento.publisher.representation;

public record AtualizacaoPedido(
        Long codigo,
        StatusPedido status,
        String urlNotaFiscal
) {
}
