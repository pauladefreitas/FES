import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class VeiculoTest {

    @Test
    public void testConstrutor() {
        Veiculo veiculo = new Veiculo("ABC123");
        assertEquals("ABC123", veiculo.getPlaca());
        assertNull(veiculo.getVaga());
        assertNull(veiculo.getCliente());
    }

    @Test
    public void testEstacionar() {
        Veiculo veiculo = new Veiculo("ABC123");
        Vaga vaga = new Vaga("V1");
        LocalDateTime entrada = LocalDateTime.now();

        veiculo.estacionar(vaga, entrada);

        assertEquals(vaga, veiculo.getVaga());
    }

    @Test
    public void testSair() {
        Veiculo veiculo = new Veiculo("ABC123");
        Vaga vaga = new Vaga("V1");
        Cliente cliente = new Cliente("Teste","1");
        LocalDateTime entrada = LocalDateTime.now();
        veiculo.setCliente(cliente);
        veiculo.estacionar(vaga, entrada);

        LocalDateTime saida = entrada.plusHours(2);
        double valorPago = veiculo.sair(saida);

        assertTrue(valorPago > 0);
        assertNull(veiculo.getVaga());
    }

    @Test
    public void testTotalArrecadado() {
        Veiculo veiculo = new Veiculo("ABC123");
        Cliente cliente = new Cliente("Teste","1");
        Vaga vaga = new Vaga("V1");
        LocalDateTime entrada = LocalDateTime.now();
        veiculo.setCliente(cliente);
        veiculo.estacionar(vaga, entrada);

        LocalDateTime saida = entrada.plusHours(2);
        double valorPago = veiculo.sair(saida);

        assertEquals(valorPago, veiculo.totalArrecadado(), 0.01);
    }

    @Test
    public void testTotalDeUsos() {
        Veiculo veiculo = new Veiculo("ABC123");
        Vaga vaga = new Vaga("V1");
        Cliente cliente = new Cliente("Teste","1");
        LocalDateTime entrada1 = LocalDateTime.now().minusDays(2);
        LocalDateTime saida1 = entrada1.plusHours(1);
        LocalDateTime entrada2 = LocalDateTime.now().minusDays(1);
        LocalDateTime saida2 = entrada2.plusHours(2);

        veiculo.estacionar(vaga, entrada1);
        veiculo.setCliente(cliente);
        veiculo.sair(saida1);
        veiculo.estacionar(vaga, entrada2);
        veiculo.sair(saida2);

        assertEquals(2, veiculo.totalDeUsos());
    }

    @Test
    public void testSetCliente() {
        Veiculo veiculo = new Veiculo("ABC123");
        Cliente cliente = new Cliente("1", "Cliente Teste");

        veiculo.setCliente(cliente);

        assertEquals(cliente, veiculo.getCliente());
    }

    @Test
    public void testSetPlaca() {
        Veiculo veiculo = new Veiculo("ABC123");
        veiculo.setPlaca("XYZ789");

        assertEquals("XYZ789", veiculo.getPlaca());
    }

    @Test
    public void testSetVaga() {
        Veiculo veiculo = new Veiculo("ABC123");
        Vaga vaga = new Vaga("V1");

        veiculo.setVaga(vaga);

        assertEquals(vaga, veiculo.getVaga());
    }

    @Test
    public void testGetUltimoUso() {
        Veiculo veiculo = new Veiculo("ABC123");
        Vaga vaga = new Vaga("V1");
        LocalDateTime entrada = LocalDateTime.now();
        veiculo.estacionar(vaga, entrada);

        UsoDeVaga ultimoUso = veiculo.getUltimoUso();

        assertNotNull(ultimoUso);
        assertEquals(entrada, ultimoUso.getEntrada());
    }
}
