# Tipos e Valores em Java

## Tipos Primitivos e Wrapper Classes (Boxing)

Java possui tipos primitivos e suas respectivas classes wrapper. O processo de converter entre eles é chamado de boxing/unboxing.

### Tipos Primitivos

```java
byte    // 8 bits
short   // 16 bits
int     // 32 bits
long    // 64 bits
float   // 32 bits, ponto flutuante
double  // 64 bits, ponto flutuante
boolean // true/false
char    // 16 bits, caractere Unicode
```

### Wrapper Classes

```java
Byte
Short
Integer
Long
Float
Double
Boolean
Character
```

### Exemplo de Boxing/Unboxing

```java
// Autoboxing (primitivo -> wrapper)
int numero = 42;
Integer numeroWrapper = numero; // automaticamente convertido

// Unboxing (wrapper -> primitivo)
Integer valor = new Integer(10);
int valorPrimitivo = valor; // automaticamente convertido

// Exemplo prático em coleções
List<Integer> numeros = new ArrayList<>();
numeros.add(10); // autoboxing acontece aqui
int primeiro = numeros.get(0); // unboxing acontece aqui
```

## Enumerações (Enum)

Enums são tipos especiais que representam um conjunto fixo de constantes.

```java
public enum TipoConta {
    CORRENTE("CC", 0.02),
    POUPANCA("CP", 0.05),
    INVESTIMENTO("CI", 0.01);

    private final String codigo;
    private final double taxaJuros;

    TipoConta(String codigo, double taxaJuros) {
        this.codigo = codigo;
        this.taxaJuros = taxaJuros;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getTaxaJuros() {
        return taxaJuros;
    }
}

// Usando o enum
TipoConta tipo = TipoConta.CORRENTE;
System.out.println(tipo.getCodigo()); // Imprime "CC"
```

## Datas (java.time)

O pacote java.time (introduzido no Java 8) oferece classes modernas para trabalhar com datas:

```java
// Data sem hora
LocalDate hoje = LocalDate.now();
LocalDate amanha = hoje.plusDays(1);
LocalDate dataEspecifica = LocalDate.of(2024, Month.MARCH, 15);

// Data com hora
LocalDateTime agora = LocalDateTime.now();
LocalDateTime futuro = agora.plusHours(2);

// Período entre datas
Period periodo = Period.between(hoje, amanha);

// Formatação
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String dataFormatada = hoje.format(formatter);
```

## Valores Monetários (BigDecimal)

Para cálculos financeiros, sempre use BigDecimal em vez de double/float para evitar problemas de arredondamento.

```java
public class ContaBancaria {
    private BigDecimal saldo;

    public ContaBancaria() {
        this.saldo = BigDecimal.ZERO;
    }

    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        this.saldo = this.saldo.add(valor);
    }

    public void calcularJuros(BigDecimal taxa) {
        // Usando scale e RoundingMode para controlar precisão
        BigDecimal juros = this.saldo.multiply(taxa)
            .setScale(2, RoundingMode.HALF_EVEN);
        this.saldo = this.saldo.add(juros);
    }
}

// Exemplo de uso
BigDecimal valor = new BigDecimal("100.50");
BigDecimal taxa = new BigDecimal("0.05");
```

## Dicas Importantes

1. **Comparação de Valores**

```java
// Nunca compare BigDecimal com equals()
BigDecimal valor1 = new BigDecimal("1.00");
BigDecimal valor2 = new BigDecimal("1.000");

// Errado
valor1.equals(valor2); // retorna false

// Correto
valor1.compareTo(valor2) == 0; // retorna true
```

2. **Criação de BigDecimal**

```java
// Prefira String no construtor
BigDecimal correto = new BigDecimal("0.1");

// Evite double no construtor
BigDecimal problematico = new BigDecimal(0.1); // pode causar imprecisão
```

3. **Formatação de Valores Monetários**

```java
NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
BigDecimal valor = new BigDecimal("1234.56");
String valorFormatado = formatoMoeda.format(valor); // R$ 1.234,56
```

4. **Constantes Úteis**

```java
BigDecimal.ZERO
BigDecimal.ONE
BigDecimal.TEN
```

5. **Operações Matemáticas**

```java
BigDecimal a = new BigDecimal("10");
BigDecimal b = new BigDecimal("3");

BigDecimal soma = a.add(b);
BigDecimal subtracao = a.subtract(b);
BigDecimal multiplicacao = a.multiply(b);
BigDecimal divisao = a.divide(b, 2, RoundingMode.HALF_EVEN);
```
