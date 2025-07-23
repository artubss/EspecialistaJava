
import java.util.Scanner;

public class Desafio02 {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        String nome = entrada.nextLine();
        System.out.printf("Olá, %s! esta é a sua folha de pagamentos!%n", nome);

        System.out.println("Digite sua carga horária mensal: ");

        double cargaHorariaMensal = entrada.nextDouble();

        System.out.println("Digite o valor da sua hora trabalhada: ");
        double valorHoraTrabalhada = entrada.nextDouble();
        double salarioMensal = cargaHorariaMensal * valorHoraTrabalhada;

        System.out.println("Sua carga horária mensal é: " + cargaHorariaMensal);
        System.out.printf("O valor da sua hora trabalhada é: %.2f%n", valorHoraTrabalhada);
        System.out.printf("Seu salário mensal é: %.2f%n", salarioMensal);

    }
}