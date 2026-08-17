import java.text.NumberFormat
import java.util.Locale

private val moeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

class Produto(
    val id: Int,
    val nome: String,
    val preco: Double,
    var estoque: Int
) {
    init {
        require(id > 0) { "O id do produto deve ser positivo." }
        require(nome.isNotBlank()) { "O nome do produto não pode estar vazio." }
        require(preco >= 0) { "O preço não pode ser negativo." }
        require(estoque >= 0) { "O estoque não pode ser negativo." }
    }

    fun exibirDetalhes() {
        println("ID: $id | Produto: $nome | Preço: ${moeda.format(preco)} | Estoque: $estoque")
    }
}

class Cliente(
    val id: Int,
    val nome: String,
    var saldo: Double
) {
    init {
        require(id > 0) { "O id do cliente deve ser positivo." }
        require(nome.isNotBlank()) { "O nome do cliente não pode estar vazio." }
        require(saldo >= 0) { "O saldo inicial não pode ser negativo." }
    }

    fun adicionarSaldo(valor: Double) {
        require(valor > 0) { "O valor adicionado deve ser maior que zero." }
        saldo += valor
        println("Saldo adicionado. Novo saldo de $nome: ${moeda.format(saldo)}")
    }
}

class CarrinhoDeCompras {
    private val itens = mutableMapOf<Produto, Int>()

    fun adicionarProduto(produto: Produto, quantidade: Int) {
        require(quantidade > 0) { "A quantidade deve ser maior que zero." }

        val quantidadeAtual = itens[produto] ?: 0
        val novaQuantidade = quantidadeAtual + quantidade

        if (novaQuantidade > produto.estoque) {
            println("Não foi possível adicionar $quantidade unidade(s) de ${produto.nome}: estoque insuficiente.")
            return
        }

        itens[produto] = novaQuantidade
        println("$quantidade unidade(s) de ${produto.nome} adicionada(s) ao carrinho.")
    }

    fun removerProduto(produto: Produto) {
        if (itens.remove(produto) != null) {
            println("${produto.nome} removido do carrinho.")
        } else {
            println("${produto.nome} não está no carrinho.")
        }
    }

    fun exibirCarrinho() {
        if (itens.isEmpty()) {
            println("O carrinho está vazio.")
            return
        }

        println("\n--- Carrinho de compras ---")
        itens.forEach { (produto, quantidade) ->
            val subtotal = produto.preco * quantidade
            println("${produto.nome} | Quantidade: $quantidade | Subtotal: ${moeda.format(subtotal)}")
        }
        println("Total: ${moeda.format(calcularTotal())}")
    }

    fun calcularTotal(): Double = itens.entries.sumOf { (produto, quantidade) ->
        produto.preco * quantidade
    }

    internal fun obterItens(): Map<Produto, Int> = itens.toMap()

    internal fun limpar() = itens.clear()
}

class Loja(private val produtos: List<Produto>) {
    fun listarProdutos() {
        println("\n--- Produtos disponíveis ---")
        produtos.forEach { it.exibirDetalhes() }
    }

    fun finalizarCompra(cliente: Cliente, carrinho: CarrinhoDeCompras) {
        val itens = carrinho.obterItens()
        if (itens.isEmpty()) {
            println("Compra cancelada: o carrinho está vazio.")
            return
        }

        val itemSemEstoque = itens.entries.firstOrNull { (produto, quantidade) ->
            quantidade > produto.estoque
        }
        if (itemSemEstoque != null) {
            println("Compra cancelada: estoque insuficiente para ${itemSemEstoque.key.nome}.")
            return
        }

        val total = carrinho.calcularTotal()
        if (cliente.saldo < total) {
            println(
                "Compra cancelada: saldo insuficiente. " +
                    "Total: ${moeda.format(total)} | Saldo: ${moeda.format(cliente.saldo)}"
            )
            return
        }

        cliente.saldo -= total
        itens.forEach { (produto, quantidade) -> produto.estoque -= quantidade }
        carrinho.limpar()

        println("Compra finalizada com sucesso para ${cliente.nome}!")
        println("Valor pago: ${moeda.format(total)} | Saldo restante: ${moeda.format(cliente.saldo)}")
    }
}

fun main() {
    val notebook = Produto(1, "Notebook", 3_500.00, 3)
    val mouse = Produto(2, "Mouse sem fio", 120.00, 10)
    val teclado = Produto(3, "Teclado mecânico", 350.00, 5)

    val loja = Loja(listOf(notebook, mouse, teclado))
    val cliente = Cliente(1, "Kamily", 4_000.00)
    val carrinho = CarrinhoDeCompras()

    loja.listarProdutos()
    carrinho.adicionarProduto(notebook, 1)
    carrinho.adicionarProduto(mouse, 2)
    carrinho.exibirCarrinho()
    loja.finalizarCompra(cliente, carrinho)
    loja.listarProdutos()
}
