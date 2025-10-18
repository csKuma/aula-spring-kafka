package io.github.cursodsousa.icompras.produtos.comtroller;

import io.github.cursodsousa.icompras.produtos.model.Produto;
import io.github.cursodsousa.icompras.produtos.repository.ProdutoRepository;
import io.github.cursodsousa.icompras.produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> novoProduto(@RequestBody Produto produto) {
        service.salvar(produto);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> buscaProduto(@PathVariable Long codigo) {
        return service.obterPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        var produto = service.obterPorCodigo(codigo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto não existe"));
        service.excluir(produto);
        return ResponseEntity.noContent().build();
    }
}
