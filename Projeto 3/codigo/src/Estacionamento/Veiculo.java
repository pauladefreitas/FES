import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Veiculo {
    private String placa;
    private List<UsoDeVaga> usos;
    private Vaga vaga;
    private Cliente cliente;
    private double custo;
    
    public Veiculo(String placa) {
        this.placa = placa;
        this.usos = new ArrayList<>();
        this.custo = 0.0;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public void estacionar(Vaga vaga, LocalDateTime entrada) {
        UsoDeVaga uso = new UsoDeVaga(vaga);
        uso.usarVaga(this, entrada);
        usos.add(uso);
        this.vaga = vaga;
    }
    
    public Vaga getVaga() {
        return this.vaga;
    }
    
    public UsoDeVaga getUltimoUso() {
        if (!usos.isEmpty()) {
            return usos.get(usos.size() - 1);
        } else {
            return null;
        }
    }
    
    public double sair(LocalDateTime saida) {
        double valorPagar = 0.0;
        UsoDeVaga ultimoUso = getUltimoUso();
        if (ultimoUso != null && ultimoUso.getVaga().equals(vaga)) {
            valorPagar = ultimoUso.sair(saida);
            vaga = null; // Define a vaga como nula após a saída
        }
        setCusto(valorPagar);
        return valorPagar;
    }
    
    public double totalArrecadado() {
        this.custo = usos.stream().mapToDouble(u -> u.calcularCusto(this, u.getEntrada(), u.getSaida())).sum();
        return custo;
    }
    
    public double arrecadadoNoMes(int mes) {
        return usos.stream()
                .filter(u -> u.getEntrada().getMonthValue() == mes)
                .mapToDouble(u -> u.calcularCusto(this, u.getEntrada(), u.getSaida()))
                .sum();
    }
    
    public int totalDeUsos() {
        return usos.size();
    }
    
    public void setVaga(Vaga vagaDisponivel) {
        this.vaga = vagaDisponivel;
    }
    
    public void setCusto(double custo) {
        this.custo = custo;
    }
    
    public double getCusto() {
        return this.custo;
    }
}
