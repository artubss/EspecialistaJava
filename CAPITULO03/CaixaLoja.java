import java.util.Scanner;

public class CaixaLoja {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Digite o valor da compra: ");
        double precoProdutos = entrada.nextDouble();

        System.out.println("cobrar frete? true/false");
        boolean cobrarFrete = entrada.nextBoolean();

        if (cobrarFrete) {
            System.out.print("valor do frete: ");
            double valorFrete = entrada.nextDouble();
            precoProdutos += valorFrete;
        } else {
            System.out.printf("valor total da compra: %.2f%n", precoProdutos);
        }

        double valorTotal = precoProdutos;

        System.out.printf("valor total da compra já com o frete: %.2f%n", valorTotal);

        entrada.close();
    }
}