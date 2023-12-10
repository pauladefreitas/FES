import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public enum ModalidadeCliente {
        HORISTA, DE_TURNO, MENSALISTA
    }
    public enum Turno {
        MANHA, TARDE, NOITE
    }
    private String nome;
    private String id;
    private List<Veiculo> veiculos;
    private ModalidadeCliente modalidade;
    private Turno turnoEscolhido;
    
    public Cliente(String nome, String id) {
        this.nome = nome;
        this.id = id;
        this.veiculos = new ArrayList<>();
    }
   
    public ModalidadeCliente getModalidade() {
        return modalidade;
    }
    
    public void setModalidade(ModalidadeCliente modalidade) {
        this.modalidade = modalidade;
    }
    
    public Turno getTurnoEscolhido() {
        return turnoEscolhido;
    }
    
    public void setTurnoEscolhido(Turno turnoEscolhido) {
        this.turnoEscolhido = turnoEscolhido;
    }
    
    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
    
    public String getNome() {
        return nome;
    }
    

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
   
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void addVeiculo(Veiculo veiculo) {
        if (veiculos.size() < 10) {
            veiculos.add(veiculo);
        } else {
            System.out.println("Limite de veículos atingido para este cliente."); // EXCECAO
        }
    }
    
    public Veiculo possuiVeiculo(String placa) {
        return veiculos.stream()
                .filter(veiculo -> veiculo.getPlaca().equals(placa))
                .findFirst()
                .orElse(null);
    }
    
    public int totalDeUsos() {
        return veiculos.stream()
                .mapToInt(Veiculo::totalDeUsos)
                .sum();
    }
   
    public double arrecadadoPorVeiculo(String placa) {
        Veiculo veiculo = possuiVeiculo(placa);
        return (veiculo != null) ? veiculo.totalArrecadado() : 0.0;
    }

public double arrecadadoTotal() {
    double totalArrecadado = veiculos.stream()
            .mapToDouble(Veiculo::totalArrecadado)
            .sum();
    switch (modalidade) {
        case DE_TURNO:
            totalArrecadado += 200.0;
            break;
        case MENSALISTA:
            totalArrecadado += 500.0;
            break;
        default:
            break;
    }
    return totalArrecadado;
}
    
    public double arrecadadoNoMes(int mes) {
        double arrecadadoMes = veiculos.stream()
                .mapToDouble(veiculo -> veiculo.arrecadadoNoMes(mes))
                .sum();
        switch (modalidade) {
            case HORISTA:
                return arrecadadoMes;
            case DE_TURNO:
                return arrecadadoMes + 200.0;
            case MENSALISTA:
                arrecadadoMes = 500.0;
                break;
        }
        return arrecadadoMes;
    }
    
    public double calcularArrecadacaoMediaHoristas(int mes) {
        return veiculos.stream()
                .filter(veiculo -> veiculo.getCliente().getModalidade() == ModalidadeCliente.HORISTA)
                .mapToDouble(veiculo -> veiculo.arrecadadoNoMes(mes))
                .average()
                .orElse(0.0);
    }
    
    public double calcularMediaUsosMensalistas() {
        return veiculos.stream()
                .filter(veiculo -> veiculo.getCliente().getModalidade() == ModalidadeCliente.MENSALISTA)
                .mapToInt(Veiculo::totalDeUsos)
                .average()
                .orElse(0.0);
    }
}
