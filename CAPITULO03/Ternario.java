public class Ternario {
    public static void main (String[] args) {
        char tipoNotaFiscal = 'M';
        double totalFaturado = 1000.30;

        double valorImpostos = tipoNotaFiscal == 'P' ? totalFaturado * 0.16 : totalFaturado * 0.35;

        System.out.printf("Total faturado: R$ %.2f%n", totalFaturado);
        System.out.printf("Total impostos: R$ %.2f%n", valorImpostos);
    }
}