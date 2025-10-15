package io.github.cursodsousa.icompras.pedidos.client;

import io.github.cursodsousa.icompras.pedidos.client.representation.ClientRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente", url = "${icompras.pedidos.clients.clientes.url}")
public interface ClientesClient {

    @GetMapping("/{codigo}")
    ResponseEntity<ClientRepresentation> obter(@PathVariable("codigo") Long codigo);

}
