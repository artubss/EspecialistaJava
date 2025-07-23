
import java.util.Scanner;

public class Desafio03 {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Digite o ano e saiba se é um ano bissexto ou não: ");
        
        int ano = entrada.nextInt();

        boolean anoBissexto = ano % 400 == 0 || ano % 4 == 0 && ano % 100 != 0;

        if (anoBissexto) {
            System.out.printf("O ano %d é bissexto", ano);
        } else {
            System.out.printf("O ano %d não é bissexto: ", ano);
        }
        entrada.close();
    }
}