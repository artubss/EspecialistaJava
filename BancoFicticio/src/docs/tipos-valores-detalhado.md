# Tipos e Valores em Java

Este documento explora em detalhes os tipos e valores em Java, com exemplos práticos do projeto BancoFicticio.

## Tipos Primitivos

Java possui oito tipos primitivos incorporados na linguagem. Estes tipos são os blocos fundamentais de construção para armazenar dados simples.

### Tipos Inteiros

Java oferece quatro tipos de dados para armazenar valores inteiros, cada um com diferentes tamanhos e intervalos:

| Tipo  | Tamanho | Intervalo                                      | Uso Típico                          |
|-------|---------|------------------------------------------------|------------------------------------|
| byte  | 8 bits  | -128 a 127                                     | Economizar memória em arrays grandes |
| short | 16 bits | -32.768 a 32.767                               | Economizar memória quando byte é insuficiente |
| int   | 32 bits | -2.147.483.648 a 2.147.483.647                 | Valor padrão para inteiros          |
| long  | 64 bits | -9.223.372.036.854.775.808 a 9.223.372.036.854.775.807 | Valores inteiros muito grandes     |

```java
// Exemplos de declaração e inicialização
byte idadePequena = 30;
short populacaoCidade = 30000;
int populacaoPais = 211000000;  // 211 milhões
long populacaoMundial = 7800000000L;  // 7.8 bilhões (note o 'L' no final)

// Literais em diferentes bases
int decimal = 42;       // Base 10 (padrão)
int hexadecimal = 0x2A; // Base 16 (começa com 0x)
int binario = 0b101010; // Base 2 (começa com 0b, disponível a partir do Java 7)
int octal = 052;        // Base 8 (começa com 0)

// Separador de dígitos (a partir do Java 7)
int umMilhao = 1_000_000; // Mais legível que 1000000
```

### Tipos de Ponto Flutuante

Para representar números com parte decimal, Java oferece dois tipos:

| Tipo   | Tamanho | Precisão                | Uso Típico                        |
|--------|---------|-------------------------|-----------------------------------|
| float  | 32 bits | ~7 dígitos decimais     | Economizar memória quando a precisão não é crítica |
| double | 64 bits | ~15 dígitos decimais    | Valor padrão para números decimais |

```java
// Exemplos de declaração e inicialização
float altura = 1.75f;  // Note o 'f' no final
double pi = 3.14159265359;

// Notação científica
double eletronsNoUniverso = 1.0e80;  // 1.0 × 10^80
```

### Tipo Caractere

O tipo `char` representa um único caractere Unicode de 16 bits:

```java
char letra = 'A';
char digitoUnicode = '\u0041';  // 'A' em Unicode
char simbolo = '€';
```

### Tipo Booleano

O tipo `boolean` tem apenas dois valores possíveis: `true` e `false`:

```java
boolean contaAtiva = true;
boolean saldoNegativo = false;
```

## Problemas com Tipos Primitivos em Cálculos Financeiros

Em aplicações financeiras como o projeto BancoFicticio, **não** utilizamos `float` ou `double` para representar valores monetários devido a problemas de precisão:

```java
// Problema de precisão com double
double valor = 0.1 + 0.2;
System.out.println(valor);  // Saída: 0.30000000000000004

// Comparação problemática
double a = 0.3;
double b = 0.1 + 0.2;
System.out.println(a == b);  // Saída: false
```

Este comportamento ocorre porque números de ponto flutuante são armazenados em formato binário, e alguns números decimais não podem ser representados com precisão em binário, assim como 1/3 não pode ser representado com precisão em decimal.

## Wrapper Classes (Classes Empacotadoras)

Para cada tipo primitivo, Java fornece uma classe correspondente no pacote `java.lang`:

| Tipo Primitivo | Wrapper Class |
|----------------|---------------|
| byte           | Byte          |
| short          | Short         |
| int            | Integer       |
| long           | Long          |
| float          | Float         |
| double         | Double        |
| char           | Character     |
| boolean        | Boolean       |

### Vantagens das Wrapper Classes

1. **Uso em coleções genéricas**: As coleções em Java (como `ArrayList`, `HashMap`) só podem armazenar objetos, não tipos primitivos
2. **Métodos utilitários**: As wrapper classes fornecem métodos úteis
3. **Valor null**: Podem representar a ausência de valor (null), enquanto primitivos não
4. **Uso em generics**: Necessário para parametrização de tipos

### Autoboxing e Unboxing

Java realiza automaticamente a conversão entre tipos primitivos e suas wrapper classes:

```java
// Autoboxing (primitivo -> wrapper)
Integer numero = 42;  // Implicitamente: Integer numero = Integer.valueOf(42);

// Unboxing (wrapper -> primitivo)
int valor = numero;   // Implicitamente: int valor = numero.intValue();

// Em coleções
List<Integer> numeros = new ArrayList<>();
numeros.add(10);  // Autoboxing de int para Integer
int primeiro = numeros.get(0);  // Unboxing de Integer para int
```

### Métodos Úteis das Wrapper Classes

```java
// Conversão de String para número
int i = Integer.parseInt("42");
double d = Double.parseDouble("3.14");

// Conversão entre tipos
String binario = Integer.toBinaryString(42);  // "101010"
String hexa = Integer.toHexString(42);        // "2a"

// Constantes
int maxInt = Integer.MAX_VALUE;  // 2147483647
int minInt = Integer.MIN_VALUE;  // -2147483648

// Comparação (a partir do Java 7)
int resultado = Integer.compare(x, y);  // -1 se x < y, 0 se x == y, 1 se x > y
```

## BigDecimal para Cálculos Financeiros

No projeto BancoFicticio, utilizamos `BigDecimal` para representar valores monetários, garantindo precisão nos cálculos financeiros.

### Criação de BigDecimal

```java
// A forma recomendada é criar a partir de String para evitar imprecisões
BigDecimal valor1 = new BigDecimal("10.50");

// Alternativas (menos recomendadas para valores exatos)
BigDecimal valor2 = BigDecimal.valueOf(10.50);  // Melhor que usar double diretamente
BigDecimal valor3 = new BigDecimal(10.50);      // Pode introduzir imprecisão!

// Constantes úteis
BigDecimal zero = BigDecimal.ZERO;
BigDecimal um = BigDecimal.ONE;
BigDecimal dez = BigDecimal.TEN;
```

### Exemplo no Projeto: Uso de BigDecimal

```java
public class ContaCorrente extends Conta {
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");
    
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
}
```

### Operações com BigDecimal

```java
BigDecimal a = new BigDecimal("10.50");
BigDecimal b = new BigDecimal("3.25");

// Operações básicas
BigDecimal soma = a.add(b);            // 13.75
BigDecimal diferenca = a.subtract(b);  // 7.25
BigDecimal produto = a.multiply(b);    // 34.125
BigDecimal quociente = a.divide(new BigDecimal("2.5"), 2, RoundingMode.HALF_EVEN);  // 4.20

// Comparação
int comparacao = a.compareTo(b);  // 1 (a > b)
boolean igual = a.equals(b);      // false

// NUNCA use equals para comparar BigDecimal com mesmo valor numérico mas escalas diferentes
BigDecimal x = new BigDecimal("1.0");
BigDecimal y = new BigDecimal("1.00");
System.out.println(x.equals(y));        // false (escalas diferentes)
System.out.println(x.compareTo(y) == 0); // true (mesmo valor numérico)
```

### Arredondamento com BigDecimal

O controle preciso de arredondamento é crucial em aplicações financeiras:

```java
BigDecimal valor = new BigDecimal("10.126");

// Diferentes modos de arredondamento
BigDecimal v1 = valor.setScale(2, RoundingMode.HALF_UP);    // 10.13 (arredonda para cima se >= 0.5)
BigDecimal v2 = valor.setScale(2, RoundingMode.HALF_DOWN);  // 10.12 (arredonda para baixo se <= 0.5)
BigDecimal v3 = valor.setScale(2, RoundingMode.HALF_EVEN);  // 10.12 (arredondamento bancário)
BigDecimal v4 = valor.setScale(2, RoundingMode.DOWN);       // 10.12 (trunca)
BigDecimal v5 = valor.setScale(2, RoundingMode.UP);         // 10.13 (arredonda para longe de zero)
```

O `RoundingMode.HALF_EVEN` (também conhecido como "arredondamento bancário" ou "arredondamento de Gauss") é o padrão recomendado para cálculos financeiros, pois minimiza o viés de arredondamento.

### Exemplo no Projeto: Arredondamento

```java
public class ContaCorrente extends Conta {
    private BigDecimal limiteChequeEspecial;
    
    public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }
}
```

## Enumerações (Enum)

Enumerações são tipos especiais que representam um conjunto fixo de constantes nomeadas.

### Enum Básico

```java
public enum DiaDaSemana {
    DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO
}

// Uso
DiaDaSemana hoje = DiaDaSemana.QUARTA;
```

### Enum com Atributos e Métodos

No projeto BancoFicticio, temos um exemplo mais complexo com o enum `TipoInvestimento`:

```java
public enum TipoInvestimento {
    RENDA_FIXA("RF", new BigDecimal("0.015"), new BigDecimal("0.001")),
    RENDA_VARIAVEL("RV", new BigDecimal("0.175"), new BigDecimal("0.002")),
    TESOURO_DIRETO("TD", new BigDecimal("0.225"), new BigDecimal("0.0005"));
    
    private final String codigo;
    private final BigDecimal aliquotaImposto;
    private final BigDecimal taxaAdministracao;
    
    TipoInvestimento(String codigo, BigDecimal aliquotaImposto, BigDecimal taxaAdministracao) {
        this.codigo = codigo;
        this.aliquotaImposto = aliquotaImposto;
        this.taxaAdministracao = taxaAdministracao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public BigDecimal getAliquotaImposto() {
        return aliquotaImposto;
    }
    
    public BigDecimal getTaxaAdministracao() {
        return taxaAdministracao;
    }
}
```

### Métodos Úteis de Enum

```java
// Obter todos os valores de um enum
TipoInvestimento[] tipos = TipoInvestimento.values();

// Converter String para enum
TipoInvestimento tipo = TipoInvestimento.valueOf("RENDA_FIXA");

// Obter nome como string
String nome = TipoInvestimento.TESOURO_DIRETO.name();  // "TESOURO_DIRETO"

// Obter posição ordinal (índice baseado em zero)
int ordinal = TipoInvestimento.RENDA_VARIAVEL.ordinal();  // 1
```

### Enum em Switch

```java
TipoInvestimento tipo = TipoInvestimento.RENDA_FIXA;

switch (tipo) {
    case RENDA_FIXA:
        System.out.println("Baixo risco");
        break;
    case RENDA_VARIAVEL:
        System.out.println("Alto risco");
        break;
    case TESOURO_DIRETO:
        System.out.println("Risco soberano");
        break;
}
```

### Enum com Métodos Abstratos

É possível definir métodos abstratos em enums, forçando cada constante a fornecer sua própria implementação:

```java
public enum OperacaoBancaria {
    DEPOSITO {
        @Override
        public void executar(Conta conta, BigDecimal valor) {
            conta.depositar(valor);
        }
    },
    SAQUE {
        @Override
        public void executar(Conta conta, BigDecimal valor) {
            conta.sacar(valor);
        }
    },
    RENDIMENTO {
        @Override
        public void executar(Conta conta, BigDecimal valor) {
            if (conta instanceof ContaPoupanca) {
                ((ContaPoupanca) conta).calcularRendimento();
            }
        }
    };
    
    public abstract void executar(Conta conta, BigDecimal valor);
}

// Uso
OperacaoBancaria.DEPOSITO.executar(conta, new BigDecimal("100.00"));
```

## Datas e Tempo em Java

Java oferece várias APIs para trabalhar com datas e horas. No projeto BancoFicticio, utilizamos a API moderna `java.time` introduzida no Java 8.

### Classes Principais do java.time

| Classe             | Descrição                                  | Exemplo de Uso                                    |
|--------------------|--------------------------------------------|-------------------------------------------------|
| LocalDate          | Data sem hora (ano, mês, dia)              | Data de nascimento, data de vencimento          |
| LocalTime          | Hora sem data (hora, minuto, segundo)      | Horário de abertura, horário de fechamento      |
| LocalDateTime      | Data e hora combinadas                     | Timestamp de transação, data de criação         |
| ZonedDateTime      | Data e hora com fuso horário               | Eventos em diferentes fusos horários            |
| Instant            | Ponto na linha do tempo (timestamp)        | Timestamp de sistema, medição precisa de tempo  |
| Duration           | Quantidade de tempo (horas, minutos, etc.) | Duração de um processo                          |
| Period             | Quantidade de tempo em datas (anos, meses) | Idade, tempo entre datas                        |

### Exemplo no Projeto: LocalDate e LocalDateTime

```java
public class Cliente {
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    // ...
}

public class Conta {
    private String numero;
    private BigDecimal saldo;
    private Cliente titular;
    private LocalDateTime dataCriacao;
    // ...
    
    public Conta(String numero, Cliente titular) {
        // ...
        this.dataCriacao = LocalDateTime.now();
        // ...
    }
}
```

### Criação de Datas

```java
// Data atual
LocalDate hoje = LocalDate.now();

// Data específica
LocalDate dataNascimento = LocalDate.of(1990, 5, 15);
LocalDate outroFormato = LocalDate.of(1990, Month.MAY, 15);

// A partir de String
LocalDate dataTexto = LocalDate.parse("2023-08-25");

// Data e hora atual
LocalDateTime agora = LocalDateTime.now();

// Data e hora específica
LocalDateTime dataHora = LocalDateTime.of(2023, 8, 25, 14, 30, 0);
```

### Manipulação de Datas

```java
LocalDate hoje = LocalDate.now();

// Adição e subtração
LocalDate amanha = hoje.plusDays(1);
LocalDate semanaPassada = hoje.minusWeeks(1);
LocalDate proximoMes = hoje.plusMonths(1);
LocalDate anoPassado = hoje.minusYears(1);

// Modificação de componentes
LocalDate primeiroDiaMes = hoje.withDayOfMonth(1);
LocalDate mesmoMesAnoPassado = hoje.withYear(hoje.getYear() - 1);

// Verificações
boolean isAntesDe = hoje.isBefore(amanha);
boolean isDepoisDe = hoje.isAfter(anoPassado);
boolean isBissexto = hoje.isLeapYear();
```

### Cálculos com Datas

```java
LocalDate dataNascimento = LocalDate.of(1990, 5, 15);
LocalDate hoje = LocalDate.now();

// Período entre datas
Period periodo = Period.between(dataNascimento, hoje);
int anos = periodo.getYears();
int meses = periodo.getMonths();
int dias = periodo.getDays();

// Dias totais entre datas
long diasTotais = ChronoUnit.DAYS.between(dataNascimento, hoje);
```

### Formatação de Datas

```java
LocalDate data = LocalDate.of(2023, 8, 25);
LocalDateTime dataHora = LocalDateTime.of(2023, 8, 25, 14, 30, 0);

// Formatação usando padrões predefinidos
DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String dataFormatada = data.format(formatadorData);  // "25/08/2023"

DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
String dataHoraFormatada = dataHora.format(formatadorDataHora);  // "25/08/2023 14:30"

// Formatação localizada
DateTimeFormatter formatadorLocalizado = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("pt", "BR"));
String dataLocalizada = data.format(formatadorLocalizado);  // "25 agosto 2023"
```

## Strings em Java

Strings são sequências imutáveis de caracteres, representadas pela classe `String`.

### Criação de Strings

```java
// Literal de string
String nome = "João da Silva";

// Construtor (menos comum)
String sobrenome = new String("Silva");

// Concatenação
String nomeCompleto = nome + " " + sobrenome;
```

### Imutabilidade de Strings

Strings em Java são imutáveis - uma vez criadas, não podem ser alteradas:

```java
String s = "original";
s.toUpperCase();  // Isso NÃO altera 's'
System.out.println(s);  // Ainda imprime "original"

// Para alterar, é preciso reatribuir
s = s.toUpperCase();
System.out.println(s);  // Agora imprime "ORIGINAL"
```

### Métodos Úteis de String

```java
String texto = "  Banco Ficticio  ";

// Comprimento
int tamanho = texto.length();  // 17

// Acesso a caracteres
char primeiroChar = texto.charAt(2);  // 'B'

// Remoção de espaços
String semEspacos = texto.trim();  // "Banco Ficticio"

// Alteração de caso
String maiusculas = texto.toUpperCase();  // "  BANCO FICTICIO  "
String minusculas = texto.toLowerCase();  // "  banco ficticio  "

// Substituição
String substituido = texto.replace("Ficticio", "Real");  // "  Banco Real  "

// Verificações
boolean comecaCom = texto.trim().startsWith("Banco");  // true
boolean terminaCom = texto.trim().endsWith("Ficticio");  // true
boolean contem = texto.contains("Fict");  // true

// Extração de substrings
String parte = texto.substring(2, 7);  // "Banco"

// Divisão
String[] palavras = texto.trim().split(" ");  // ["Banco", "Ficticio"]
```

### Comparação de Strings

```java
String s1 = "banco";
String s2 = "Banco";
String s3 = "banco";
String s4 = new String("banco");

// Igualdade de conteúdo
boolean igual1 = s1.equals(s2);       // false (case sensitive)
boolean igual2 = s1.equals(s3);       // true
boolean igual3 = s1.equalsIgnoreCase(s2);  // true (ignora case)

// Comparação de referências (geralmente não recomendado para Strings)
boolean ref1 = (s1 == s3);  // pode ser true (pool de strings)
boolean ref2 = (s1 == s4);  // false (objetos diferentes)

// Comparação lexicográfica
int comp1 = s1.compareTo(s2);  // positivo ('b' vem depois de 'B')
int comp2 = s1.compareTo(s3);  // zero (igual)
```

### StringBuilder e StringBuffer

Para construção eficiente de strings, use `StringBuilder` (não sincronizado) ou `StringBuffer` (sincronizado):

```java
// Construção de string com muitas concatenações
StringBuilder sb = new StringBuilder();
sb.append("Cliente: ").append(cliente.getNome());
sb.append(", CPF: ").append(cliente.getCpf());
sb.append(", Saldo: ").append(conta.getSaldo());
String resultado = sb.toString();
```

## Tipos Genéricos (Generics)

Generics permitem criar classes, interfaces e métodos que operam com tipos parametrizados, proporcionando maior segurança de tipo em tempo de compilação.

### Exemplo de Classe Genérica

```java
public class Caixa<T> {
    private T conteudo;
    
    public void guardar(T item) {
        this.conteudo = item;
    }
    
    public T obter() {
        return conteudo;
    }
}

// Uso
Caixa<String> caixaDeTexto = new Caixa<>();
caixaDeTexto.guardar("Documento importante");
String documento = caixaDeTexto.obter();

Caixa<BigDecimal> caixaDeDinheiro = new Caixa<>();
caixaDeDinheiro.guardar(new BigDecimal("1000.00"));
BigDecimal dinheiro = caixaDeDinheiro.obter();
```

### Métodos Genéricos

```java
public <T extends Comparable<T>> T encontrarMaior(List<T> lista) {
    if (lista == null || lista.isEmpty()) {
        return null;
    }
    
    T maior = lista.get(0);
    for (T item : lista) {
        if (item.compareTo(maior) > 0) {
            maior = item;
        }
    }
    return maior;
}

// Uso
List<Integer> numeros = Arrays.asList(5, 2, 8, 1, 9);
Integer maiorNumero = encontrarMaior(numeros);  // 9

List<String> nomes = Arrays.asList("Ana", "Carlos", "Bruno", "Diana");
String maiorNome = encontrarMaior(nomes);  // "Diana" (ordem alfabética)
```

### Wildcards em Generics

```java
// Wildcard desconhecido (?)
public void processar(List<?> lista) {
    // Pode ler elementos como Object
    for (Object o : lista) {
        System.out.println(o);
    }
    // Não pode adicionar elementos (exceto null)
}

// Wildcard limitado superior (extends)
public double calcularSaldoTotal(List<? extends Conta> contas) {
    double total = 0;
    for (Conta c : contas) {
        total += c.getSaldo().doubleValue();
    }
    return total;
}

// Wildcard limitado inferior (super)
public void adicionarContasCorrente(List<? super ContaCorrente> destino) {
    destino.add(new ContaCorrente("1111", cliente));
    destino.add(new ContaCorrente("2222", cliente));
}
```

## Valores Nulos e Optional

O tratamento adequado de valores nulos é crucial para evitar `NullPointerException`.

### Verificação Tradicional de Nulos

```java
// Abordagem tradicional
public String obterNomeCliente(Conta conta) {
    if (conta == null) {
        return "Cliente não disponível";
    }
    
    Cliente titular = conta.getTitular();
    if (titular == null) {
        return "Cliente não disponível";
    }
    
    String nome = titular.getNome();
    if (nome == null) {
        return "Nome não disponível";
    }
    
    return nome;
}
```

### Optional (Java 8+)

A classe `Optional` fornece uma maneira mais elegante de lidar com valores potencialmente nulos:

```java
// Usando Optional
public String obterNomeClienteOptional(Optional<Conta> contaOpt) {
    return contaOpt
            .map(Conta::getTitular)
            .map(Cliente::getNome)
            .orElse("Cliente não disponível");
}

// Criando Optionals
Optional<Conta> contaOpt = Optional.ofNullable(conta);  // pode ser null
Optional<Cliente> clienteOpt = Optional.of(cliente);    // não pode ser null
Optional<String> vazio = Optional.empty();              // Optional vazio

// Verificando se há valor
if (contaOpt.isPresent()) {
    // Há uma conta
}

// Executando código se houver valor
contaOpt.ifPresent(conta -> {
    System.out.println("Processando conta: " + conta.getNumero());
});

// Obtendo valor ou alternativa
Conta conta = contaOpt.orElse(new ContaCorrente("0000", clientePadrao));
Conta conta2 = contaOpt.orElseGet(() -> criarContaPadrao());
Conta conta3 = contaOpt.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
```

## Conclusão

O entendimento profundo dos tipos e valores em Java é fundamental para o desenvolvimento de aplicações robustas e precisas, especialmente em sistemas financeiros como o BancoFicticio. A escolha correta dos tipos (como `BigDecimal` para valores monetários) e o tratamento adequado de valores (como datas e strings) são essenciais para garantir a integridade e precisão dos dados.

Pontos-chave:
1. Use tipos primitivos para valores simples e eficiência
2. Use `BigDecimal` para cálculos financeiros precisos
3. Aproveite as classes modernas de data e hora do pacote `java.time`
4. Lembre-se da imutabilidade de `String` e use `StringBuilder` quando necessário
5. Utilize generics para criar código type-safe e reutilizável
6. Considere `Optional` para lidar com valores potencialmente nulos de forma mais elegante 