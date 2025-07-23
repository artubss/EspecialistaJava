// Aposta da megasena
import java.util.Scanner;
import java.util.Random;

public class DoWhile {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Quantidade de números da aposta: ");
        int quantidadeNumeros = entrada.nextInt();

        int[] aposta = new int[quantidadeNumeros];

        for (int NumeroDaAposta = 0; NumeroDaAposta < quantidadeNumeros; NumeroDaAposta++) {
            imprimirTraco();
            System.out.println("Número " + (NumeroDaAposta + 1) + ": ");
            aposta[NumeroDaAposta] = entrada.nextInt();
            imprimirTraco();
            if (NumeroDaAposta == quantidadeNumeros - 1) {
                apostaFeita(aposta);
                int[] numerosSorteados = sortearNumeros();
                verificarResultado(aposta, numerosSorteados);
            }
        }
        entrada.close();
    }
        private static void imprimirTraco() {
            System.out.println("--------------------------------");
        }
        private static void apostaFeita(int[] aposta) {
            System.out.println("Aposta feita com sucesso!");
            imprimirTraco();
            System.out.printf("Número de apostas: %d%n", aposta.length);
            imprimirTraco();
            for (int NumeroDaAposta = 0; NumeroDaAposta < aposta.length; NumeroDaAposta++) {
                System.out.println("Aposta " + (NumeroDaAposta + 1) + ": " + aposta[NumeroDaAposta]);
            }
            imprimirTraco();
            System.out.println("Fim da aposta");
        }
        private static int[] sortearNumeros() {
            System.out.println("Sorteando números...");
            imprimirTraco();
            Random random = new Random();
            int[] numerosSorteados= new int[6];
            boolean numeroRepetido;
            for (int i = 0; i < 6; i++) {
                int numeroSorteado = random.nextInt(60) + 1; //numeros sorteados podem ir de 1 a 60
                numeroRepetido = false;
                for (int NumeroJaSorteado = 0; NumeroJaSorteado < i; NumeroJaSorteado++) {
                    if (numerosSorteados[NumeroJaSorteado] == numeroSorteado) {
                        numeroRepetido = true;
                        break;
                    }
                }
            }
                System.out.println("Números sorteados: ");
                return numerosSorteados;
        }
        private static void verificarResultado(int[] aposta, int[] numerosSorteados) {
            System.out.println("Verificando resultado...");
            imprimirTraco();
            //contar quantos numeros acertou
            int acertos = 0;
            for (int i = 0; i < aposta.length; i++) {
                for (int j = 0; j < numerosSorteados.length; j++) {
                    if (aposta[i] == numerosSorteados[j]) {
                        acertos++;
                        System.out.println("Acertou o número " + aposta[i]);
                    }
                }
            }
            System.out.println("Números acertados: " + acertos);
            imprimirTraco();
            System.out.println("Fim da verificação");
        }
        private static void imprimirNumerosSorteados(int[] numerosSorteados) {
            System.out.println("Números sorteados: ");
            for (int i = 0; i < numerosSorteados.length; i++) {
                System.out.println(numerosSorteados[i]);
            }
        }
    }