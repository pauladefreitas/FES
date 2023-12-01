//adicionado ao classpath;
public class VeiculoJaExistenteException extends Exception {
    public VeiculoJaExistenteException() {
        super("Veículo já existente para este cliente.");
    }
}