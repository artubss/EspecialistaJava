public class Carro {
    String fabricante;
    String modelo;
    String cor;
    int anoFabricacao;
    double preco;

    public Carro(String fabricante, String modelo, String cor, int ano, double preco) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.cor = cor;
        this.anoFabricacao = ano;
        this.preco = preco;
    }

    public void exibirDetalhes() {
        System.out.println("Fabricante: " + fabricante);
        System.out.println("Modelo: " + modelo);
        System.out.println("Cor: " + cor);
        System.out.println("Ano de fabricação: " + anoFabricacao);
        System.out.println("Preço: " + preco);
    }
}
