import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa o uso de uma vaga de estacionamento por um cliente.
 */
public class UsoDeVaga {

    private static final double FRACAO_USO = 0.25;
    private static final double VALOR_FRACAO = 4.0;
    private static final double VALOR_MAXIMO = 50.0;

    private Vaga vaga;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private double valorPago;
    private List<Servico> servicosContratados;
    private Cliente cliente;

    private static final Map<Veiculo, Vaga> veiculoVagaMap = new HashMap<>();

    /**
     * Construtor que cria uma instância de UsoDeVaga associada a uma vaga.
     *
     * @param vaga A vaga utilizada no uso.
     */
    public UsoDeVaga(Vaga vaga) {
        this.vaga = vaga;
        this.valorPago = 0.0;
        this.servicosContratados = new ArrayList<>();
    }

    /**
     * Obtém a data e hora de entrada na vaga.
     *
     * @return A data e hora de entrada.
     */
    public LocalDateTime getEntrada() {
        return entrada;
    }

    /**
     * Obtém a data e hora de entrada na vaga.
     *
     * @return A data e hora de entrada.
     */
    public LocalDateTime getSaida() {
        return saida;
    }

    /**
     * Obtém a vaga associada ao uso.
     *
     * @return A vaga associada.
     */
    public Vaga getVaga() {
        return this.vaga;
    }

    /**
     * Define o cliente associado ao uso.
     *
     * @param cliente O cliente associado ao uso.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Registra o uso da vaga com a data e hora de entrada especificadas.
     *
     * @param veiculo O veículo utilizando a vaga.
     * @param entrada A data e hora de entrada na vaga.
     */
    public void usarVaga(Veiculo veiculo, LocalDateTime entrada) {
        if (this.entrada == null) {
            this.entrada = entrada;
            veiculoVagaMap.put(veiculo, vaga);
        }
    }

    /**
     * Registra a saída do veículo da vaga com a data e hora de saída especificadas.
     *
     * @param saida A data e hora de saída da vaga.
     * @return O valor a ser pago pelo uso da vaga.
     */
    public double sair(LocalDateTime saida) {
        this.saida = saida;
    
        Vaga vagaDoUso = this.vaga;
    
        if (vagaDoUso != null) {
            Veiculo veiculoDoUso = veiculoVagaMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(vagaDoUso))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
    
            if (veiculoDoUso != null) {
                veiculoVagaMap.remove(veiculoDoUso);
                vagaDoUso.disponivel();
    
                double valorCusto = calcularCusto(veiculoDoUso, this.entrada, this.saida);
                double valorServicos = calcularCustoServicos();
    
                this.valorPago = valorCusto + valorServicos;
                return this.valorPago;

            }
        }
    
        return this.valorPago;
    }

    /**
     * Contrata um serviço adicional para o uso de uma vaga de estacionamento.
     *
     * @param servico O serviço a ser contratado.
     */
    public void contratarServico(Servico servico) {
        if (servicosContratados == null) {
            servicosContratados = new ArrayList<>();
        }

        if (!servicosContratados.contains(servico)) {
            servicosContratados.add(servico);
            System.out.println("Serviço contratado com sucesso: " + servico.getDescricao());
        } else {
            System.out.println("Este serviço já foi contratado anteriormente.");
        }
    }

    
    /**
     * Calcula o custo total dos serviços adicionais contratados.
     *
     * @return O custo total dos serviços adicionais.
     */
    private double calcularCustoServicos() {
            return this.servicosContratados.stream()
                    .mapToDouble(Servico::getValor)
                    .sum();
    }

    /**
     * Calcula o custo total para o uso de uma vaga de estacionamento considerando o tipo de cliente.
     *
     * @param veiculo  O veículo utilizado.
     * @param entrada  A data e hora de entrada.
     * @param saida    A data e hora de saída.
     * @return O custo total do uso da vaga.
     */
    public double calcularCusto(Veiculo veiculo, LocalDateTime entrada, LocalDateTime saida) {
        Cliente cliente = veiculo.getCliente();

        if (cliente != null) {
            switch (cliente.getModalidade()) {
                case HORISTA:
                    return calcularCustoHorista(entrada, saida) + calcularCustoServicos();
                case DE_TURNO:
                    return calcularCustoDeTurno(entrada, saida, cliente) + calcularCustoServicos();
                case MENSALISTA:
                    return calcularCustoServicos();
            }
        }

        return VALOR_FRACAO;
    }

    /**
     * Calcula o custo para clientes horistas, levando em consideração o tempo de estacionamento.
     *
     * @param entrada A data e hora de entrada na vaga.
     * @param saida A data e hora de saída da vaga.
     * @return O custo para clientes horistas.
     */
    private double calcularCustoHorista(LocalDateTime entrada, LocalDateTime saida) {
        if (entrada == null || saida == null) {
            return VALOR_FRACAO;
        }

        Duration duracao = Duration.between(entrada, saida);

        duracao = duracao.plusMinutes(15 - (duracao.toMinutes() % 15));

        long minutosEstacionados = duracao.toMinutes();

        if (minutosEstacionados <= 60) {
            return Math.min(VALOR_FRACAO, VALOR_MAXIMO);
        } else {
            double valorExcedente = Math.ceil((minutosEstacionados - 60) / 15.0) * FRACAO_USO * VALOR_FRACAO;
            return Math.min(valorExcedente, VALOR_MAXIMO);
        }
    }

    /**
     * Calcula o custo para clientes de turno, levando em consideração o turno escolhido.
     *
     * @param entrada A data e hora de entrada na vaga.
     * @param saida A data e hora de saída da vaga.
     * @param cliente O cliente utilizando a vaga.
     * @return O custo para clientes de turno.
     */
    private double calcularCustoDeTurno(LocalDateTime entrada, LocalDateTime saida, Cliente cliente) {
        if (estaDentroDoTurno(entrada, cliente)) {
            // Custo é zero dentro do turno
            return 0.0;
        } else {
            // Fora do turno, calcula como cliente horista
            return calcularCustoHorista(entrada, saida);
        }
    }

    /**
     * Verifica se o horário está dentro do turno escolhido pelo cliente.
     *
     * @param horario O horário a ser verificado.
     * @param cliente O cliente utilizando a vaga.
     * @return true se estiver dentro do turno, false caso contrário.
     */
    private boolean estaDentroDoTurno(LocalDateTime horario, Cliente cliente) {
        int hora = horario.getHour();

        switch (cliente.getModalidade()) {
            case DE_TURNO:
                switch (cliente.getTurnoEscolhido()) {
                    case MANHA:
                        return hora >= 8 && hora <= 12;
                    case TARDE:
                        return hora > 12 && hora <= 18;
                    case NOITE:
                        return hora > 18 && hora <= 23;
                }
                break;
            default:
                break;
        }

        return false;
    }

    /**
     * Obtém o cliente associado ao uso da vaga.
     *
     * @return O cliente associado ao uso da vaga.
     */
    public Cliente getCliente() {
        return this.cliente;
    }

    /**
     * Obtém a lista de serviços adicionais contratados.
     *
     * @return A lista de serviços adicionais contratados.
     */
    public List<Servico> getServicosContratados() {
        return servicosContratados;
    }
}
