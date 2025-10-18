package io.github.cursodsousa.icompras.logistica.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsousa.icompras.logistica.service.EnvioPedidoService;
import io.github.cursodsousa.icompras.logistica.subscriber.representation.AtualizacaoFaturamentoPedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FaturamentoSubscriber {
    private final EnvioPedidoService service;

    private final ObjectMapper mapper;

    @KafkaListener(groupId = "icompras-logistica",
            topics = "${icompras.config.kafka.topics.pedidos-faturados}")
    private void listen(String json) {
        try {
            var representation = mapper.readValue(json, AtualizacaoFaturamentoPedidoRepresentation.class);

            service.enviar(representation.codigo(), representation.urlNotaFiscal());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
