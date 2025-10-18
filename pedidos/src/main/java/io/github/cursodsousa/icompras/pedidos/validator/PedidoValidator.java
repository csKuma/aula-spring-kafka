package io.github.cursodsousa.icompras.pedidos.validator;

import feign.FeignException;
import io.github.cursodsousa.icompras.pedidos.client.ClientesClient;
import io.github.cursodsousa.icompras.pedidos.client.ProdutosClient;
import io.github.cursodsousa.icompras.pedidos.client.representation.ClientRepresentation;
import io.github.cursodsousa.icompras.pedidos.client.representation.ProdutoRepresentation;
import io.github.cursodsousa.icompras.pedidos.exception.ValidationException;
import io.github.cursodsousa.icompras.pedidos.model.ItemPedido;
import io.github.cursodsousa.icompras.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CompositeTypeRegistration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {
    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido) {
//        List<Long> codigosProdutos = pedido.getItens().stream().map(ItemPedido::getCodigo).toList();

        validarCliente(pedido.getCodigoCliente());
        pedido.getItens().forEach(this::validarProduto);
    }

    private void validarCliente(Long codigoCliente) {
        try {
            ResponseEntity<ClientRepresentation> obter = clientesClient.obter(codigoCliente);
            ClientRepresentation clienteRepresentation = obter.getBody();
            if (!clienteRepresentation.ativo()){
                var mensagem = String.format("Cliente de codigo %d  está inativo", codigoCliente);
                throw new ValidationException("codigoCliente", mensagem.toString());
            }
        } catch (FeignException.NotFound e) {
            var mensagem = String.format("Cliente de codigo %d não encontrado", codigoCliente);
            throw new ValidationException("codigoCliente", mensagem.toString());
        }

    }

    private void validarProduto(ItemPedido itemPedido) {
        try {
            ResponseEntity<ProdutoRepresentation> produto = produtosClient.obterDadosProduto(itemPedido.getCodigoProduto());
            ProdutoRepresentation body = produto.getBody();
            if (!body.ativo()) {
                var mensagem = String.format("Produto de codigo %d não está mais ativo", itemPedido.getCodigoProduto());
                throw new ValidationException("codigoProduto", mensagem.toString());
            }
        } catch (FeignException.NotFound e) {
            var mensagem = String.format("Produto de codigo %d não encontrado", itemPedido.getCodigoProduto());
            throw new ValidationException("codigoProduto", mensagem.toString());
        }
    }
}
