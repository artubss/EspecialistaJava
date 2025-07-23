public class Comparacao {
    public static void main (String [] args) {
        int pesoProdutos = 1800;
        int limitePesoCaminhao = 1000;

        boolean cargaExcedida = pesoProdutos > limitePesoCaminhao;
        int excesso = pesoProdutos - limitePesoCaminhao;

        if (cargaExcedida) {
            System.out.printf("Carga excedida: %d kg%n", excesso);
        } else {
            System.out.println("Carga dentro do limite");
        }
}}