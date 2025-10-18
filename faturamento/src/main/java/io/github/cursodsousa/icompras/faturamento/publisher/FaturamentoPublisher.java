package io.github.cursodsousa.icompras.faturamento.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsousa.icompras.faturamento.model.Pedido;
import io.github.cursodsousa.icompras.faturamento.publisher.representation.AtualizacaoPedido;
import io.github.cursodsousa.icompras.faturamento.publisher.representation.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FaturamentoPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${icompras.config.kafka.topics.pedidos-faturados}")
    private String tipico;

    public void publicar(Pedido pedido, String urlNotaFiscal) {
        try {
            var representation = new AtualizacaoPedido(pedido.codigo(), StatusPedido.FATURADO, urlNotaFiscal);
            String json = mapper.writeValueAsString(representation);
            kafkaTemplate.send(tipico, json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }


}
