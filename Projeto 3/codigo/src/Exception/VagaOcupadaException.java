//adicionado ao classpath;
public class VagaOcupadaException extends Exception {
    public VagaOcupadaException() {
        super("A vaga já está ocupada por outro veículo.");
    }
}