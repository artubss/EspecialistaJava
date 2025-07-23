
import java.util.Scanner;

public class ExameCortisol {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Digite o valor do cortisol: ");
        double cortisol = entrada.nextDouble();
//cortisol ideal é de 6-18.4 mcg/dl
        boolean cortisolNormal = cortisol >= 6 && cortisol <= 18.6;

        if (cortisolNormal) {
            System.out.printf("Cortisol normal de: %.2f, parabéns, vc não é estressado! %n", cortisol);
        } else {
            System.out.printf("Cortisol fora do normal de: %b, vc está estressado! %n", cortisolNormal);
        }

        entrada.close();
    }
}