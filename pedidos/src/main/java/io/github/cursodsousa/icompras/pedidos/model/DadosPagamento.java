package io.github.cursodsousa.icompras.pedidos.model;

import io.github.cursodsousa.icompras.pedidos.model.enums.TipoPagamento;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DadosPagamento {
    private String dados;
    private TipoPagamento tipoPagamento;
}
