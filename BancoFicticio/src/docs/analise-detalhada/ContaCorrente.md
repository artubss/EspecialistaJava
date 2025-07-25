# Análise Detalhada: ContaCorrente.java

## Visão Geral

A classe `ContaCorrente` é uma implementação concreta de `Conta` que representa uma conta corrente bancária, com funcionalidades específicas como cheque especial e tarifa mensal fixa.

## Análise Linha a Linha

```java
package com.banco.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.banco.exception.SaldoInsuficienteException;
```

- Importações necessárias
- `RoundingMode` para controle de arredondamento
- Exceção específica para saldo insuficiente

```java
public class ContaCorrente extends Conta {
```

- Herança da classe abstrata `Conta`
- Implementação concreta de conta bancária

```java
    private BigDecimal limiteChequeEspecial;
    private static final BigDecimal TARIFA_MENSAL = new BigDecimal("30.00");
```

- Atributo específico para limite do cheque especial
- Constante para tarifa mensal
- Uso de `static final` para valor fixo
- Valor monetário como String para precisão

```java
    public ContaCorrente(String numero, Cliente titular) {
        this(numero, titular, BigDecimal.ZERO);
    }
```

- Construtor conveniente
- Delega para construtor principal
- Limite zero por padrão

```java
    public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
        super(numero, titular);
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }
```

- Construtor principal
- Chama construtor da superclasse
- Configura limite com escala correta
- Arredondamento bancário (HALF_EVEN)

```java
    @Override
    public void sacar(BigDecimal valor) {
        BigDecimal saldoTotal = getSaldo().add(limiteChequeEspecial);
        if (valor.compareTo(saldoTotal) > 0) {
            throw new SaldoInsuficienteException(
                "Saldo e limite insuficientes para saque",
                getSaldo(),
                valor
            );
        }
        super.sacar(valor);
    }
```

- Sobrescrita do método sacar
- Considera o limite do cheque especial
- Validação específica para saldo + limite
- Exceção com contexto detalhado
- Chama implementação da superclasse

```java
    @Override
    public void calcularTarifaMensal() {
        setSaldo(getSaldo().subtract(TARIFA_MENSAL));
    }
```

- Implementação do método abstrato
- Cobra tarifa fixa mensal
- Usa setter protegido da superclasse

```java
    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial.setScale(2, RoundingMode.HALF_EVEN);
    }
```

- Getters e setters para limite
- Mantém consistência de escala
- Arredondamento padronizado

```java
    @Override
    public String toString() {
        return "ContaCorrente{"
                + "numero='" + getNumero() + '\''
                + ", saldo=" + getSaldo()
                + ", limiteChequeEspecial=" + limiteChequeEspecial
                + ", titular=" + getTitular().getNome()
                + '}';
    }
```

- Representação textual personalizada
- Inclui informações específicas
- Formato legível e completo

## Conceitos Demonstrados

1. **Herança**

   - Extensão da classe Conta
   - Sobrescrita de métodos
   - Chamada de métodos da superclasse

2. **Polimorfismo**

   - Implementação específica de calcularTarifaMensal
   - Comportamento especializado de saque

3. **Encapsulamento**

   - Atributos privados
   - Métodos públicos controlados
   - Validações de dados

4. **Precisão Monetária**
   - Uso consistente de BigDecimal
   - Controle de escala
   - Arredondamento apropriado

## Características Específicas

1. **Cheque Especial**

   - Limite adicional para saque
   - Validação considerando limite
   - Flexibilidade no valor do limite

2. **Tarifa Fixa**
   - Valor constante mensal
   - Implementação simples
   - Facilmente modificável

## Pontos de Extensão Possíveis

1. **Juros do Cheque Especial**

   - Calcular juros sobre limite usado
   - Taxas diferenciadas
   - Período de uso

2. **Limites Dinâmicos**

   - Ajuste automático de limite
   - Critérios de aumento/redução
   - Histórico de uso

3. **Tarifas Variáveis**

   - Pacotes de serviços
   - Isenções por saldo médio
   - Descontos por relacionamento

4. **Notificações**

   - Aviso de uso do limite
   - Alerta de tarifa
   - Comunicação com cliente

5. **Análise de Crédito**
   - Cálculo de limite disponível
   - Score do cliente
   - Histórico de movimentação
