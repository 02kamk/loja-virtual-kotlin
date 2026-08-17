# Loja Virtual em Kotlin

Atividade de Introdução ao Kotlin e Programação Orientada a Objetos da disciplina de Soluções Mobile (UNISATC).

## Objetivo

Simular uma loja virtual em console. A solução contém as classes `Produto`, `Cliente`, `CarrinhoDeCompras` e `Loja`, com controle de saldo, quantidades e estoque.

## Funcionalidades

- cadastro e exibição de produtos;
- recarga do saldo do cliente;
- adição e remoção de produtos do carrinho;
- cálculo do total da compra;
- validação de saldo e estoque;
- atualização do saldo e do estoque após o pagamento.

## Como executar

Com o compilador Kotlin instalado, execute na raiz do projeto:

```bash
kotlinc src/Main.kt -include-runtime -d loja-virtual.jar
java -jar loja-virtual.jar
```

O método `main` contém um cenário de demonstração que lista produtos, adiciona itens ao carrinho e finaliza uma compra.

## Estrutura

```text
.
├── src/
│   └── Main.kt
├── .gitignore
└── README.md
```
