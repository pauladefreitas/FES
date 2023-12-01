import org.junit.Test;
import static org.junit.Assert.*;

public class EstacionamentoTest {

    @Test
    public void testAdicionarCliente() throws ClienteJaExistenteException {
        Estacionamento estacionamento = new Estacionamento("Estacionamento Teste", 3, 5);
        Cliente cliente = new Cliente("1", "Cliente Teste");

        // Adicionar cliente
        estacionamento.addCliente(cliente);

        // Verificar se o cliente foi adicionado corretamente
        assertTrue(estacionamento.getClientes().contains(cliente));
    }

    @Test(expected = ClienteJaExistenteException.class)
    public void testAdicionarClienteExistente() throws ClienteJaExistenteException {
        Estacionamento estacionamento = new Estacionamento("Estacionamento Teste", 3, 5);
        Cliente cliente = new Cliente("1", "Cliente Teste");

        // Adicionar cliente pela primeira vez
        estacionamento.addCliente(cliente);

        // Tentar adicionar o mesmo cliente novamente, deve lançar exceção
        estacionamento.addCliente(cliente);
    }

    @Test
    public void testAdicionarVeiculo() throws VeiculoJaExistenteException, ClienteNaoExisteException {
        Estacionamento estacionamento = new Estacionamento("Estacionamento Teste", 3, 5);
        Cliente cliente = new Cliente("1", "Cliente Teste");
        try {
            estacionamento.addCliente(cliente);
        } catch (ClienteJaExistenteException e) {
            e.printStackTrace();
        }

        Veiculo veiculo = new Veiculo("ABC123");

        estacionamento.addVeiculo(veiculo, cliente.getId());

        assertTrue(cliente.possuiVeiculo("ABC123") != null);
    }

    @Test(expected = ClienteNaoExisteException.class)
    public void testAdicionarVeiculoClienteNaoExistente() throws VeiculoJaExistenteException, ClienteNaoExisteException {
        Estacionamento estacionamento = new Estacionamento("Estacionamento Teste", 3, 5);
        Veiculo veiculo = new Veiculo("ABC123");

        estacionamento.addVeiculo(veiculo, "ClienteInexistente");
    }


}
