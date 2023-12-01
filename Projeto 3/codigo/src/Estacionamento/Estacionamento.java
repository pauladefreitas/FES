import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
/**
 * Classe que representa um estacionamento e suas operações relacionadas a clientes, veículos e vagas.
 */
public class Estacionamento implements IDataToText {

    private String nome;
    private List<Cliente> clientes;
    private List<Vaga> vagas;
    private int quantFileiras;
    private int vagasPorFileira;
    
    private Map<Veiculo, UsoDeVaga> veiculoUsoMap;

    /**
     * Construtor que cria uma instância de estacionamento com o nome e configurações especificados.
     *
     * @param nome            O nome do estacionamento.
     * @param fileiras        A quantidade de fileiras de vagas no estacionamento.
     * @param vagasPorFila    O número de vagas por fileira.
     */
    public Estacionamento(String nome, int fileiras, int vagasPorFila) {
        this.nome = nome;
        this.quantFileiras = fileiras;
        this.vagasPorFileira = vagasPorFila;
        this.clientes = new ArrayList<>();
        this.vagas = new ArrayList<>();
        this.veiculoUsoMap = new HashMap<>();
        gerarVagas();
    }

    /**
     * Adiciona um veículo a um cliente existente no estacionamento.
     *
     * @param veiculo O veículo a ser adicionado.
     * @param idCli   O identificador do cliente ao qual o veículo será associado.
     */
    public void addVeiculo(Veiculo veiculo, String idCli) throws VeiculoJaExistenteException, ClienteNaoExisteException {
        Cliente cliente = encontrarCliente(idCli);
        if (cliente != null) {
            if (cliente.possuiVeiculo(veiculo.getPlaca()) != null) {
                throw new VeiculoJaExistenteException();
            } else {
                cliente.addVeiculo(veiculo);
            }
        } else {
            throw new ClienteNaoExisteException();
        }
    }

    /**
     * Adiciona um cliente ao estacionamento.
     *
     * @param cliente O cliente a ser adicionado.
     */
    public void addCliente(Cliente cliente) throws ClienteJaExistenteException {
        if (clientes.contains(cliente)) {
            throw new ClienteJaExistenteException();
        } else {
            clientes.add(cliente);
        }
    }

    private void gerarVagas() {
        int numeroVaga = 1;
        for (int fileira = 1; fileira <= quantFileiras; fileira++) {
            for (int vaga = 1; vaga <= vagasPorFileira; vaga++) {
                Vaga novaVaga = new Vaga("V" + numeroVaga);
                vagas.add(novaVaga);
                numeroVaga++;
            }
        }
    }

    public List<Vaga> getVagas() {
        return vagas;
    }

    /**
     * Estaciona um veículo em uma vaga disponível no estacionamento.
     *
     * @param placa   A placa do veículo a ser estacionado.
     * @param entrada A data e hora de entrada do veículo na vaga.
     * @throws VeiculoNaoExisteException
     */
    public void estacionar(Veiculo veiculo, Vaga vaga, LocalDateTime entrada)
            throws VagaOcupadaException, VeiculoNaoExisteException {
        veiculo.estacionar(vaga, entrada);
        UsoDeVaga uso = new UsoDeVaga(vaga);
        veiculoUsoMap.put(veiculo, uso);
    }

    /**
     * Registra a saída de um veículo do estacionamento e calcula o valor a ser pago.
     *
     * @param placa A placa do veículo que está saindo.
     * @param saida A data e hora de saída do veículo.
     * @return O valor a ser pago pelo uso da vaga.
     */
    public double sair(Veiculo veiculo, LocalDateTime saida) throws UsoDeVagaFinalizadoException, VeiculoNaoExisteException {
        return veiculo.sair(saida);
    }

    /**
     * Calcula o valor total arrecadado pelo estacionamento.
     *
     * @return O valor total arrecadado.
     */
    public double totalArrecadado() {
        return clientes.stream()
                .mapToDouble(Cliente::arrecadadoTotal)
                .sum();
    }    

    /**
     * Calcula a arrecadação total do estacionamento em um mês específico.
     *
     * @param mes O mês para o qual a arrecadação será calculada.
     * @return A arrecadação total no mês especificado.
     */
    public double arrecadacaoNoMes(int mes) {
        return clientes.stream()
                .mapToDouble(cliente -> cliente.arrecadadoNoMes(mes))
                .sum();
    }    

    /**
     * Calcula o valor médio arrecadado por uso de vaga no estacionamento.
     *
     * @return O valor médio por uso de vaga.
     */
    public double valorMedioPorUso() {
        return clientes.stream()
                .mapToDouble(Cliente::arrecadadoTotal)
                .average()
                .orElse(0.0);
    }

    /**
     * Obtém uma lista dos cinco melhores clientes em termos de arrecadação em um mês específico.
     *
     * @param mes O mês para o qual o ranking dos clientes será calculado.
     * @return Uma string que lista os cinco melhores clientes no mês especificado.
     */
    public String top5Clientes(int mes) {
        List<Cliente> topClientes = clientes.stream()
                .sorted(Comparator.comparingDouble(c -> -c.arrecadadoNoMes(mes)))
                .limit(5)
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder("Top 5 Clientes no mês " + mes + ":\n");
        topClientes.forEach(cliente -> result.append(cliente.getNome()).append(": R$ ").append(cliente.arrecadadoNoMes(mes)).append("\n"));
        return result.toString();
    }


    /**
     * Busca e retorna um cliente com base no ID fornecido.
     *
     * @param idCli O ID do cliente a ser buscado.
     * @return O cliente encontrado ou null se nenhum cliente corresponder ao ID fornecido.
     */
    public Cliente encontrarCliente(String idCli) {
        return clientes.stream()
                .filter(cliente -> cliente.getId().equals(idCli))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca e retorna um veículo com base na placa fornecida.
     *
     * @param placa A placa do veículo a ser buscado.
     * @return O veículo encontrado ou null se nenhum veículo corresponder à placa fornecida.
     */
    public Veiculo encontrarVeiculo(String placa) {
        return clientes.stream()
                .map(cliente -> cliente.possuiVeiculo(placa))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna uma string formatada com as informações do cliente encontrado pelo ID.
     *
     * @param idCli O ID do cliente a ser encontrado.
     * @return Uma string com as informações do cliente ou uma mensagem indicando que o cliente não foi encontrado.
     */
    public String formatarClienteEncontrado(String idCli) {
        Cliente clienteEncontrado = encontrarCliente(idCli);

        if (clienteEncontrado != null) {
            return String.format("Cliente encontrado:\nNome: %s\nID: %s", clienteEncontrado.getNome(), clienteEncontrado.getId());
        } else {
            return "Cliente não encontrado.";
        }
    }

    /**
     * Retorna uma string formatada com as informações do veículo encontrado pela placa.
     *
     * @param placa A placa do veículo a ser encontrado.
     * @return Uma string com as informações do veículo ou uma mensagem indicando que o veículo não foi encontrado.
     */
    public String formatarVeiculoEncontrado(String placa) {
        Veiculo veiculoEncontrado = encontrarVeiculo(placa);

        if (veiculoEncontrado != null) {
            return String.format("Veículo encontrado:\nPlaca: %s\nCliente: %s\nValor total arrecadado: R$ %.2f",
                    veiculoEncontrado.getPlaca(), veiculoEncontrado.getCliente().getNome(), veiculoEncontrado.totalArrecadado());
        } else {
            return "Veículo não encontrado.";
        }
    }

    /**
     * Contrata um serviço adicional para um veículo estacionado.
     *
     * @param placa   A placa do veículo.
     * @param servico O serviço a ser contratado.
     * @return True se o serviço foi contratado com sucesso, false caso contrário.
     */
    public String contratarServico(String placa, Servico servico) {
        Veiculo veiculo = encontrarVeiculo(placa);

        if (veiculo == null) {
            return "Veículo não encontrado.";
        }

        UsoDeVaga uso = veiculoUsoMap.get(veiculo);

        if (uso == null) {
            return "Veículo não está atualmente em uso de uma vaga.";
        }

        uso.contratarServico(servico);
        return servico +" contratado com sucesso para o veículo com placa " + placa + ".";
    }
    
    @Override
    public String dataToText() {
        StringBuilder data = new StringBuilder();
        data.append("Nome do Estacionamento: ").append(nome).append("\n");
        data.append("Quantidade de Fileiras: ").append(quantFileiras).append("\n");
        data.append("Vagas por Fileira: ").append(vagasPorFileira).append("\n\n");
    
        data.append("Clientes:\n");
        for (Cliente cliente : clientes) {
            data.append("Nome: ").append(cliente.getNome()).append(", ID: ").append(cliente.getId()).append(", Modalidade: ").append(cliente.getModalidade()).append("\n");
    
            data.append("Veículos do Cliente:\n");
            List<Veiculo> veiculos = cliente.getVeiculos();
            for (Veiculo veiculo : veiculos) {
                if (veiculo != null) {
                    data.append("Placa: ").append(veiculo.getPlaca()).append(", custando: R$").append(veiculo.getCusto()).append("0").append("\n");
                }
            }
    
            data.append("\n");
        }
    
        data.append("Vagas:\n");
        for (Vaga vaga : vagas) {
            data.append("ID da Vaga: ").append(vaga.getId()).append(", Disponível: ").append(vaga.disponivel() ? "Desocupada" : "Ocupada").append("\n");
        }
    
        return data.toString();
    }
    

    /**
     * Obtém um relatório consolidado do valor total arrecadado pelo cliente, incluindo detalhes
     * para cada veículo registrado.
     *
     * @return Um relatório consolidado do valor total arrecadado pelo cliente.
     */
    public String relatorioArrecadacaoCliente(String idCli) {
        Cliente cliente = encontrarCliente(idCli);
        StringBuilder relatorio = new StringBuilder();

        relatorio.append(String.format("Relatório de Arrecadação para o Cliente %s (ID: %s)\n", cliente.getNome(), cliente.getId()));

        for (Veiculo veiculo : cliente.getVeiculos()) {
            if (veiculo != null) {
                relatorio.append(String.format("Veículo com placa %s:\n", veiculo.getPlaca()));
                relatorio.append(String.format("- Valor total arrecadado: R$ %.2f\n", veiculo.totalArrecadado()));
            }
        }

        double valorTotalArrecadado = cliente.arrecadadoTotal();
        relatorio.append(String.format("\nValor total arrecadado pelo cliente: R$ %.2f\n", valorTotalArrecadado));

        return relatorio.toString();
    }

    /**
     * Calcula a arrecadação média dos clientes horistas neste mês usando Stream.
     *
     * @param mes O mês para o qual se deseja calcular a arrecadação média.
     * @return A arrecadação média dos clientes horistas neste mês.
     */
    public double calcularArrecadacaoMediaHoristas(int mes) {
        return clientes.stream()
                .filter(cliente -> cliente.getModalidade() == Cliente.ModalidadeCliente.HORISTA)
                .mapToDouble(cliente -> cliente.arrecadadoNoMes(mes))
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula a média de usos mensais para clientes mensalistas.
     *
     * @return A média de usos mensais para clientes mensalistas.
     */
    public double calcularMediaUsosMensalistas() {
        return clientes.stream()
                .filter(cliente -> cliente.getModalidade() == Cliente.ModalidadeCliente.MENSALISTA)
                .mapToInt(Cliente::totalDeUsos)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula a arrecadação total do estacionamento, incluindo todas as modalidades de clientes.
     *
     * @return A arrecadação total do estacionamento.
     */
    public double arrecadacaoTotalEstacionamento() {
        return clientes.stream()
                .flatMap(cliente -> cliente.getVeiculos().stream())
                .mapToDouble(Veiculo::totalArrecadado)
                .sum() +
                clientes.stream()
                        .filter(cliente -> cliente.getModalidade() == Cliente.ModalidadeCliente.MENSALISTA)
                        .mapToDouble(Cliente::arrecadadoTotal)
                        .sum();
    }

    /**
     * Obtém o nome do estacionamento.
     *
     * @return O nome do estacionamento.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Obtém a quantidade de fileiras de vagas no estacionamento.
     *
     * @return A quantidade de fileiras.
     */
    public int getQuantFileiras() {
        return quantFileiras;
    }

    /**
     * Obtém o número de vagas por fileira no estacionamento.
     *
     * @return O número de vagas por fileira.
     */
    public int getVagasPorFileira() {
        return vagasPorFileira;
    }

    /**
     * Obtém a lista de clientes do estacionamento.
     *
     * @return A lista de clientes.
     */
    public List<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Obtém o número de fileiras do estacionamento.
     *
     * @return O número de fileiras
     */
    public int getNumFileiras() {
        return this.quantFileiras;
    }
}
