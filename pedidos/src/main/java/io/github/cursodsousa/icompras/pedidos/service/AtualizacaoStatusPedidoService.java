package io.github.cursodsousa.icompras.pedidos.service;

import io.github.cursodsousa.icompras.pedidos.model.Pedido;
import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedidos;
import io.github.cursodsousa.icompras.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoService {
    private final PedidoRepository pedidoRepository;

    @Transactional
    public void atualizarStatus(Long codigo, StatusPedidos statusPedido, String urlNotaFiscal, String rastreio) {
        pedidoRepository.findById(codigo).ifPresent(pedido -> {
            pedido.setStatus(statusPedido);

            if (Objects.nonNull(urlNotaFiscal)) {
                pedido.setUrlNf(urlNotaFiscal);
            }

            if (Objects.nonNull(rastreio)) {
                pedido.setCodigoRastreio(rastreio);
            }
            pedidoRepository.save(pedido);
        });
    }
}
