package io.github.cursodsousa.icompras.pedidos.service;

import io.github.cursodsousa.icompras.pedidos.client.ClientesClient;
import io.github.cursodsousa.icompras.pedidos.client.ProdutosClient;
import io.github.cursodsousa.icompras.pedidos.client.ServicoBancarioClient;
import io.github.cursodsousa.icompras.pedidos.exception.ItemNaoEncontradoException;
import io.github.cursodsousa.icompras.pedidos.model.DadosPagamento;
import io.github.cursodsousa.icompras.pedidos.model.ItemPedido;
import io.github.cursodsousa.icompras.pedidos.model.Pedido;
import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedidos;
import io.github.cursodsousa.icompras.pedidos.model.enums.TipoPagamento;
import io.github.cursodsousa.icompras.pedidos.publisher.PagamentoPublisher;
import io.github.cursodsousa.icompras.pedidos.repository.ItemPedidoRepository;
import io.github.cursodsousa.icompras.pedidos.repository.PedidoRepository;
import io.github.cursodsousa.icompras.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator pedidoValidator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient apiCliente;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher pagamentoPublisher;

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        pedidoValidator.validar(pedido);
        realizarPersistencia(pedido);
        realizarPagamento(pedido);
        return pedido;
    }

    private void realizarPagamento(Pedido pedido) {
        String chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    @Transactional
    public void atualizarStatusPagamento(Long codigo, String chavePagamento, boolean sucesso, String observacoes) {

        pedidoRepository.findByCodigoAndChavePagamento(codigo, chavePagamento).ifPresentOrElse(
                pedido -> {
                    if (sucesso) {
                        prepararEPublicarPedidoPago(pedido);

                    } else {
                        pedido.setStatus(StatusPedidos.ERRO_PAGAMENTO);
                        pedido.setObservacoes(observacoes);
                    }
                },
                () -> {
                    var msg = String.format("Pedido não encontrato com o codigo %s e chave pagamento %s", codigo, chavePagamento);
                    log.error(msg);
                });
    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedidos.PAGO);
        carregarDadosCliente(pedido);
        carregarDadosItemPedido(pedido);
        pagamentoPublisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigo, String dadosCartao, TipoPagamento tipoPagamento) {
        pedidoRepository.findById(codigo).ifPresentOrElse(
                pedido -> {
                    DadosPagamento dadosPagamento = new DadosPagamento();
                    dadosPagamento.setTipoPagamento(tipoPagamento);
                    dadosPagamento.setDados(dadosCartao);
                    pedido.setDadosPagamento(dadosPagamento);
                    pedido.setStatus(StatusPedidos.REALIZADO);
                    pedido.setObservacoes("Novo pagamento realizado, aguardando processamento ");
                    realizarPagamento(pedido);
                    pedidoRepository.save(pedido);
                }, () -> {
                    throw new ItemNaoEncontradoException("Pedido não encontrato com o codigo informado");
                });
    }


    public Optional<Pedido> carregarDadosPedido(Long codigo) {
        Optional<Pedido> pedido = pedidoRepository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarDadosItemPedido);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido) {
        var response = apiCliente.obter(pedido.getCodigoCliente()).getBody();
        pedido.setDadosCliente(response);
    }

    private void carregarDadosItemPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido itemPedido) {
        var response = apiProdutos.obterDadosProduto(itemPedido.getCodigoProduto()).getBody();
        itemPedido.setNome(response.nome());
    }
}
