//adicionado ao classpath;
public class VeiculoNaoExisteException extends Exception {
    public VeiculoNaoExisteException() {
        super("O veículo não existe.");
    }
}