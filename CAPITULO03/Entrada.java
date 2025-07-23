
import java.util.Scanner;

public class Entrada {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Digite seu nome: ");
        String nome = entrada.nextLine();

        System.out.printf("Seu nome é %s%n", nome);

        System.out.println("Peso: ");
        double Peso = entrada.nextDouble();
        System.out.printf("Peso é: %.2f kg%n", Peso);

     }
}