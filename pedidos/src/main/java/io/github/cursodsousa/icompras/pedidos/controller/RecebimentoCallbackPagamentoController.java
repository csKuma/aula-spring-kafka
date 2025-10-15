package io.github.cursodsousa.icompras.pedidos.controller;

import io.github.cursodsousa.icompras.pedidos.controller.dto.RecebimentoCallBackPagamento;
import io.github.cursodsousa.icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/callback-pagamento")
@RequiredArgsConstructor
public class RecebimentoCallbackPagamentoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<Object> atualizarStatusPagamento(@RequestBody RecebimentoCallBackPagamento body,
                                                           @RequestHeader(required = true, name = "apiKey") String apiKey) {
        service.atualizarStatusPagamento(
                body.codigo(),
                body.chavePagamento(),
                body.status(),
                body.observacoes()
        );

        return ResponseEntity.ok().build();
    }

}
