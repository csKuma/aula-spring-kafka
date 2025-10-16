package io.github.cursodsousa.icompras.pedidos.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsousa.icompras.pedidos.controller.mappers.PedidoMapper;
import io.github.cursodsousa.icompras.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoPublisher {
    private final DetalhePedidoMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.confg.kafka.tipics.pedidos-pagos}")
    private String topico;

    public void publicar(Pedido pedido) {
        log.info("publicar pedido pago {}", pedido.getCodigo());

        try {
            var representation = mapper.map(pedido);
            var json = objectMapper.writeValueAsString(representation);
            kafkaTemplate.send(topico, "dados",json);
        } catch (JsonProcessingException e) {
            log.error("erro ao processar o json ", e);
        } catch (RuntimeException e) {
            log.error("erro tecnico ao publicar no topico de pedidos ", e);
        }
    }
}
