import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class EstacionamentoDAO implements DAO<Estacionamento> {

    private String nomeArq;
    private Scanner arqLeitura;
    private FileWriter arqEscrita;

    public EstacionamentoDAO(String nomeArq) {
        this.nomeArq = nomeArq;
        this.arqEscrita = null;
        this.arqLeitura = null;
    }

    public void abrirLeitura() throws IOException {
        if (arqEscrita != null) {
            arqEscrita.close();
            arqEscrita = null;
        }
        arqLeitura = new Scanner(new File(nomeArq), Charset.forName("UTF-8"));
    }

    public void abrirEscrita() throws IOException {
        if (arqLeitura != null) {
            arqLeitura.close();
            arqLeitura = null;
        }
        arqEscrita = new FileWriter(nomeArq, Charset.forName("UTF-8"), true);
    }

    public void fechar() throws IOException {
        if (arqEscrita != null) arqEscrita.close();
        if (arqLeitura != null) arqLeitura.close();
        arqEscrita = null;
        arqLeitura = null;
    }

    public Estacionamento getNext() {
        String nome = null;
        int numFileiras = 0;
        int vagasPorFileira = 0;

        while (arqLeitura.hasNextLine()) {
            String linha = arqLeitura.nextLine().trim();
            if (linha.startsWith("Nome do Estacionamento")) {
                nome = linha.substring("Nome do Estacionamento:".length()).trim();
            } else if (linha.startsWith("Quantidade de Fileiras")) {
                numFileiras = Integer.parseInt(linha.substring("Quantidade de Fileiras:".length()).trim());
            } else if (linha.startsWith("Vagas por Fileira")) {
                vagasPorFileira = Integer.parseInt(linha.substring("Vagas por Fileira:".length()).trim());
                break;
            }
        }

        Estacionamento estacionamento = new Estacionamento(nome, numFileiras, vagasPorFileira);

        lerClientes(estacionamento);
        lerVagas();

        return estacionamento;
    }

    public void lerClientes(Estacionamento estacionamento) {
        boolean leituraClientes = false;
    
        while (arqLeitura.hasNextLine()) {
            String linha = arqLeitura.nextLine().trim();
    
            if (linha.equals("Clientes:")) {
                leituraClientes = true;
            } else if (linha.equals("Vagas:")) {
                break;
            }
    
            if (leituraClientes && linha.startsWith("Nome: ")) {
                String nomeCliente = obterValorCampo(linha, "Nome:");
                String idCliente = obterValorCampo(linha, "ID:");
                String modalidade = obterValorCampo(linha, "Modalidade:");
    
                Cliente cliente = new Cliente(nomeCliente, idCliente);
    
                switch (modalidade) {
                    case "HORISTA":
                        cliente.setModalidade(Cliente.ModalidadeCliente.HORISTA);
                        break;
                    case "DE_TURNO":
                        cliente.setModalidade(Cliente.ModalidadeCliente.DE_TURNO);
                        break;
                    case "MENSALISTA":
                        cliente.setModalidade(Cliente.ModalidadeCliente.MENSALISTA);
                        break;
                    default:
                        break;
                }
                
                List<Veiculo> veiculos = lerVeiculos(cliente);
                cliente.setVeiculos(veiculos);
                try {
                    estacionamento.addCliente(cliente);
                } catch (ClienteJaExistenteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Veiculo> lerVeiculos(Cliente cliente) {
        List<Veiculo> veiculos = new ArrayList<>();
    
        while (arqLeitura.hasNextLine()) {
            String linha = arqLeitura.nextLine().trim();
    
            if (linha.startsWith("Placa: ")) {
                String placaVeiculo = obterValorCampo(linha, "Placa: ");
                double custoVeiculo = Double.parseDouble(obterValorCampo(linha, "R$"));

                Veiculo veiculo = new Veiculo(placaVeiculo);
                veiculo.setCliente(cliente);
                veiculo.setCusto(custoVeiculo);
                veiculos.add(veiculo);
            } else if (linha.isEmpty()) {
                break;
            }
        }
    
        return veiculos;
    }

    private List<Vaga> lerVagas() {
        List<Vaga> vagas = new ArrayList<>();
    
        while (arqLeitura.hasNextLine()) {
            String linha = arqLeitura.nextLine().trim();
    
            if (linha.startsWith("ID da Vaga: ")) {
                String idVaga = obterValorCampo(linha, "ID da Vaga: ");
                String disponibilidade = obterValorCampo(linha, "Disponível: ");
    
                Vaga vaga = new Vaga(idVaga);
    
                if (disponibilidade.equalsIgnoreCase("Ocupada")) {
                    vaga.estacionar();
                } else {
                    vaga.sair();
                }
    
                vagas.add(vaga);
            } else if (linha.isEmpty()) {
                break;
            }
        }
    
        return vagas;
    }
    
    
     
    
    private String obterValorCampo(String linha, String nomeCampo) {
        int posicaoCampo = linha.indexOf(nomeCampo);
        if (posicaoCampo != -1) {
            int posicaoVirgula = linha.indexOf(",", posicaoCampo); //ler até a vírgula para evitar info duplicada
            if (posicaoVirgula != -1) {
                return linha.substring(posicaoCampo + nomeCampo.length(), posicaoVirgula).trim();
            } else {
                return linha.substring(posicaoCampo + nomeCampo.length()).trim();
            }
        }
        return "";
    }    
    
    /**
     * Adiciona um objeto Estacionamento ao arquivo.
     *
     * @param estacionamento O objeto Estacionamento a ser adicionado ao arquivo.
     * @throws IOException Se ocorrer um erro de E/S durante a escrita no arquivo.
     */
    public void add(Estacionamento estacionamento) throws IOException {
        arqEscrita.append(estacionamento.dataToText() + "\n");
    }

    public Estacionamento[] getAll() {
        int TAM_MAX = 10000;
        int cont = 0;
        Estacionamento[] dados = new Estacionamento[TAM_MAX];
        try {
            fechar();
            abrirLeitura();
            while (arqLeitura.hasNext()) {
                dados[cont] = this.getNext();
                cont++;
            }
        } catch (IOException exception) {
            arqEscrita = null;
            arqLeitura = null;
            dados = null;
        }
        dados = Arrays.copyOf(dados, cont);
        return dados;
    }

    public void addAll(Estacionamento[] dados) {
        try {
            fechar();
            abrirEscrita();
            for (Estacionamento estacionamento : dados) {
                if (estacionamento != null)
                    add(estacionamento);
            }
        } catch (IOException e) {
            arqEscrita = null;
            arqLeitura = null;
        }
    }
}
