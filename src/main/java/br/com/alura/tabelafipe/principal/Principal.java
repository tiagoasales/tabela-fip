package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.DadosGenerico;
import br.com.alura.tabelafipe.model.Veiculos;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.service.ConsumoApi;
import br.com.alura.tabelafipe.service.ConverteDados;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class Principal {

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    private ConsumoApi consumoApi = new ConsumoApi();

    private Scanner leitor = new Scanner(System.in);

    private ConverteDados conversor = new ConverteDados();

    private ObjectMapper mapper = new ObjectMapper();

    public void exibeMenu() {

        System.out.println("**** OPÇÕES ****");
        System.out.println("CARRO");
        System.out.println("MOTO");
        System.out.println("CAMINHÃO");

        System.out.println("\nDigite uma das opção acima para consultar os valores: ");
        var opcao = leitor.nextLine();
        String endereco = "";

        if(opcao.contains("car")) {
            endereco = URL_BASE + "carros/marcas/";
        } else if(opcao.contains("mot")) {
            endereco = URL_BASE + "motos/marcas/";
        } else if(opcao.contains("cam")) {
            endereco = URL_BASE + "motos/marcas/";
        } else {
            System.out.println("opção inválida!!!");
            return;
        }

        var json = consumoApi.obterDados(endereco);
        List<Dados> marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o código da marca que deseja consultar: ");
        var marca = leitor.nextLine();

        json = consumoApi.obterDados(endereco + marca + "/modelos");
        System.out.println(json);
        var listaModelos = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca");
        listaModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o trecho do nome do veículo para consulta: ");
        var trechoNomeVeiculo = leitor.nextLine();

        // mostra os modelos filtrados pelo nome do carro
        System.out.println("\nModelos filtrados:");
        listaModelos.modelos().stream().filter(v -> v.nome().toUpperCase().contains(trechoNomeVeiculo.toUpperCase())).forEach(System.out::println);

        System.out.println("\nDigite o código do modelo para consultar os valores: ");
        var codigoModelo = leitor.nextInt();

        json = consumoApi.obterDados(endereco + marca + "/modelos/" + codigoModelo + "/anos");
        List<DadosGenerico> anos = conversor.obterLista(json, DadosGenerico.class);

        List<Veiculos> veiculos = new ArrayList<Veiculos>();

        for (DadosGenerico ano : anos) {
            json = consumoApi.obterDados(URL_BASE + opcao + "s/marcas/" + marca + "/modelos/" + codigoModelo + "/anos" + "/" + ano.codigo());
            var veiculo = conversor.obterDados(json, Veiculos.class);
            veiculos.add(veiculo);
        }

        veiculos.stream().sorted(Comparator.comparing(Veiculos::ano).reversed()).forEach(System.out::println);

    }
}
