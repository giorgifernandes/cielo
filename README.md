# CIELO

Integração com a cielo utilizando seus WebServices, 
com essa biblioteca é possível criar uma transação junto a cielo, consultar uma trasação existente cria token para o uso do cartão em transaçes recorrêntes e cancelamento de transação existentes.

## Modo de Uso

1. Criar uma instância do cliente cielo

```java
ClientCieloService clientCieloService = new ClientCieloService(ACCESSKEY,AFFILIATENUMBER,AMBIENTE);
```
- ACCESSKEY= Token fornecido pela cielo
- AFFILIATENUMBER = Seu número fornecido pela cielo
- AMBIENTE = Boleano onde será informado se é ambiente de homologação ou produção


2. Informar os dados do cartão
```java
Card card = new Card();
card.setName("Cartão teste");
card.setFlag("elo");
card.setNumber("4012001037141112");
card.setValidate("201805");
card.setSecurityCode("123");
```
- Caso já possua um token para o cartão que irá realizar o pagamento, basta informar 
```java
Card card = new Card();
card.setName("Cartão teste");
card.setToken(CARDTOKEN);
```

3. Criar o pedido
```java
Order order = new Order();
		order.setNumber("000001");
		order.setCurrencyType("986");
		order.setValue(508000);
		order.setDate(new Date());
		order.setDescription("Produto da venda");
		order.setLanguage("PT");
		order.setSoftDescription("Produto");
		order.setPaymentType("1");
		order.setInstallment(1);
		order.setRateShipping("0");
 ```
 
 4. Criar a transação, onde terá o pedido e o cartão
```java
Transaction transaction = new Transaction();
transaction.setCard(card);
transaction.setOrder(order);
```
- Caso seja necessário no momento da transação criar o token do cartão, informe: 
```java
transaction.setGenerateToken(true);
```

- Notificação de midança de status da transação
```java 
transaction.setReturnUrl("https://url.retorno.cielo");
```

5. Eviar dados para a cielo
```java
clientCieloService.createTransation(transaction)
```

## Outras opções
### Cancelar transação
Para realizar o cancelamento da transação na cielo

```java
clientCieloService.cancelTransaction(TID)
```

- TID IDentificador da trasação junto a cielo

### Consulta Status da Transação
Verifica com está o processo junto a cielo (Pendente, analise, etc)

```java
clientCieloService.consultTransaction(TID)
```

### Criar token cartão
```java
clientCieloService.createToken(card)
```

## Instalação

1. Faça download do zip extraia, entre na pasta descompactada e rode os comandos Maven
```
mvn clean
mvn package
mvn install
```
2. Adicione a dependência ao seu projeto (adicionar ao lib path ou POM.XML)
```maven
<!-- Integração cielo -->
<dependency>
     <groupId>br.com.cielo</groupId>
     <artifactId>cielo</artifactId>
     <version>1.0-SNAPSHOT</version>
</dependency>
```
