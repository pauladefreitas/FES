public class ClienteNaoExisteException extends Exception {
    //adicionado ao classpath;
    public ClienteNaoExisteException() {
        super("Cliente não existente no estacionamento.");
    }
}
