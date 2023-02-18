# User Consumption

## Sobre
O projeto aborda a criação de um pedido, o qual pode possuir uma lista de produtos e serviços vinculados a ele. Também é possível aplicar desconto na compra de produtos e acompanhar o valor total do pedido.

O projeto foi criado como um teste de conhecimento da empresa SENIOR e possui os seguintes requisitos:
- Deverá ser desenvolvido um cadastro (Create/Read/Update/Delete/List com paginação) 
para as seguintes entidades: produto/serviço, pedido e itens de pedido.
- Deverá ser possível aplicar filtros na listagem
- As entidades deverão utilizar Bean Validation
- Deverá ser implementado um ControllerAdvice para customizar os HTTP Response das 
requisições (mínimo BAD REQUEST)
- Todos as entidades deverão ter um ID único do tipo UUID gerado automaticamente
- No cadastro de produto/serviço deverá ter uma indicação para diferenciar um produto de 
um serviço
- Deverá ser possível aplicar um percentual de desconto no pedido, porém apenas para os 
itens que sejam produto (não serviço); o desconto será sobre o valor total dos produtos
- Somente será possível aplicar desconto no pedido se ele estiver na situação Aberto 
(Fechado bloqueia)
- Não deve ser possível excluir um produto/serviço se ele estiver associado a algum pedido
- Não deve ser possível adicionar um produto desativado em um pedido

## Especificações
- Apache Maven 3.6.3
- Java 8
- Spring Boot 2.7.8
- PostgreSQL 14.6

### Modelo
![alt text](https://i.imgur.com/mJffD7l.png)

Para iniciar a configuração do projeto, use os recursos da própria IDE ou digite o comando no diretório do projeto:
```
mvn clean install
```

OBS: Por conta do projeto usar o QueryDSL e ele gerar uma "cópia" (QClass) das classes criadas, pode demorar alguns segundos para essas classes aparecerem na pasta Target e talvez até mostrar um erro em linhas de código enquanto não for completamente importado.
É importante também, caso a IDE não faça isso automaticamente, marcar o path "target/generated-sources/annotations" como "Source" para que consiga identificar as classes que estão armazenadas no local.
