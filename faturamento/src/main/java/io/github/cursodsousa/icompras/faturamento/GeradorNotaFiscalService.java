package io.github.cursodsousa.icompras.faturamento;

import io.github.cursodsousa.icompras.faturamento.bucket.BucketFile;
import io.github.cursodsousa.icompras.faturamento.bucket.BucketService;
import io.github.cursodsousa.icompras.faturamento.model.Pedido;
import io.github.cursodsousa.icompras.faturamento.publisher.FaturamentoPublisher;
import io.github.cursodsousa.icompras.faturamento.service.NotaFiscalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;
    private final FaturamentoPublisher faturamentoPublisher;

    public void gerar(Pedido pedido) {
        log.info("Gerando nota fiscal do pedido: {}", pedido);

        try {
            byte[] byteArray = notaFiscalService.gerarNota(pedido);
            String nomeArquivo = String.format("notaFiscal_pedido_%d.pdf", pedido.codigo());

            var file = new BucketFile(nomeArquivo, new ByteArrayInputStream(byteArray), MediaType.APPLICATION_PDF, byteArray.length);
            bucketService.upload(file);
            String url = bucketService.getUrl(nomeArquivo);
            faturamentoPublisher.publicar(pedido, url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
