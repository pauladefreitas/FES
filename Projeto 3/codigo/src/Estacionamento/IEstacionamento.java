import java.time.LocalDateTime;

public interface IEstacionamento {
    public double sair(Vaga vaga, LocalDateTime saida);
    public void estacionar(Vaga vaga, LocalDateTime entrada);
}
