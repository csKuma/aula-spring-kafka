package io.github.cursodsousa.icompras.logistica.publisher;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsousa.icompras.logistica.publisher.representation.AtualizacaoEnvioPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnvioPedidoPublisher {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-enviados}")
    private String topico;

    public void enviar(AtualizacaoEnvioPedido atualizacao) {
        log.info("Enviando pedido enciado : {}", atualizacao.codigo());
        try {
            var json = objectMapper.writeValueAsString(atualizacao);
            kafkaTemplate.send(topico, "dados", json);
            log.info("publicando pedido enviado: {}, codigo de rastreio: {} ", atualizacao.codigo(), atualizacao.rastreio());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
