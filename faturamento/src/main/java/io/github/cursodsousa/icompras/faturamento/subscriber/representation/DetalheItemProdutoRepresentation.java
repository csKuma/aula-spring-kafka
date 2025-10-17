package io.github.cursodsousa.icompras.faturamento.subscriber.representation;

import java.math.BigDecimal;

public record DetalheItemProdutoRepresentation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario
) {
    public BigDecimal getValorTotal() {
        return valorUnitario.multiply(new BigDecimal(quantidade));
    }
}
