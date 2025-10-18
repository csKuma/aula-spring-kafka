package io.github.cursodsousa.icompras.logistica.service;

import io.github.cursodsousa.icompras.logistica.model.StatusPedido;
import io.github.cursodsousa.icompras.logistica.publisher.EnvioPedidoPublisher;
import io.github.cursodsousa.icompras.logistica.publisher.representation.AtualizacaoEnvioPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnvioPedidoService {

    private final EnvioPedidoPublisher publisher;

    public void enviar(Long codigoPedido, String urlNotaFiscal) {
        var codigo = gerarCodigoRastreio();
        var atualizacao = new AtualizacaoEnvioPedido(codigoPedido, StatusPedido.ENVIADO, urlNotaFiscal,codigo);
        publisher.enviar(atualizacao);
    }

    private String gerarCodigoRastreio() {
        //AB123456789BR
        var random = new Random();
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));
        int numero= 100000000 + random.nextInt(999999999);
        return ""+letra1+letra2+numero+"BR";
    }
}
