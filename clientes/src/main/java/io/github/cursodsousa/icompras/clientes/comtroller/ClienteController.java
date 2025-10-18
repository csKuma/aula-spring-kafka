package io.github.cursodsousa.icompras.clientes.comtroller;

import io.github.cursodsousa.icompras.clientes.model.Cliente;
import io.github.cursodsousa.icompras.clientes.repository.ClienteRepository;
import io.github.cursodsousa.icompras.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(service.cadastrar(cliente));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Cliente> buscarPorCodigo(@PathVariable Long codigo) {
        return service.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long codigo) {
        var cliente = service.buscarPorCodigo(codigo).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cliente não encontrado"
        ));
        service.excluir(cliente);
        return ResponseEntity.ok().build();
    }
}
