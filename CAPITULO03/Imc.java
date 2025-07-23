import java.util.Scanner;

public class Imc {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Digite seu peso: ");
        Double peso = entrada.nextDouble();

        System.out.println("Digite sua altura em centímetros: ");
        Double altura = entrada.nextDouble();

        double imc = peso / (altura / 100 * altura / 100);

        boolean imcNormal = imc >= 18.5 && imc <= 24.9;
        boolean imcAcima = imc >= 25;
        boolean imcAbaixo = imc < 18.5;

        if (imcNormal) {
            System.out.println("IMC normal: " + imc);
        } else if (imcAcima) {
            System.out.println("IMC acima do normal: " + imc);
        } else {
            System.out.println("IMC abaixo do normal: " + imc);
        }
    }
}