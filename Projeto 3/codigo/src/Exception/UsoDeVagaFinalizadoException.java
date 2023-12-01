//adicionado ao classpath;
public class UsoDeVagaFinalizadoException extends Exception {
    public UsoDeVagaFinalizadoException() {
        super("O uso da vaga já foi finalizado para este veículo.");
    }
}