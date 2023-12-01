//adicionado ao classpath;
public class ClienteJaExistenteException extends Exception {
    public ClienteJaExistenteException() {
        super("Cliente já existente no estacionamento.");
    }
}
