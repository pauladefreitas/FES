import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class UsoDeVagaTest {

    @Test
    public void testSair() {
        // Criação de objetos necessários para o teste
        Vaga vaga = new Vaga("V1");
        Cliente cliente = new Cliente("1", "Cliente Teste");
        Veiculo veiculo = new Veiculo("ABC-1234");

        UsoDeVaga usoDeVaga = new UsoDeVaga(vaga);
        LocalDateTime entrada = LocalDateTime.now().minusHours(1);
        LocalDateTime saida = LocalDateTime.now();

        usoDeVaga.usarVaga(veiculo, entrada);

        veiculo.setCliente(cliente);
        double valorPago = usoDeVaga.sair(saida);
        usoDeVaga.setCliente(cliente);

        assertEquals(veiculo.getCliente(), usoDeVaga.getCliente());
        assertEquals(entrada, usoDeVaga.getEntrada());
        assertEquals(saida, usoDeVaga.getSaida());
        assertTrue(valorPago > 0.0);
    }

    @Test
    public void testContratarServico() {
        
        Vaga vaga = new Vaga("V2");
        UsoDeVaga usoDeVaga = new UsoDeVaga(vaga);
        
        Servico servico = Servico.POLIMENTO;

        usoDeVaga.contratarServico(servico);

        assertTrue(usoDeVaga.getServicosContratados().contains(servico));
    }

    @Test
    public void testCalcularCusto() {
        // Criação de objetos necessários para o teste
        Vaga vaga = new Vaga("V3");
        Cliente cliente = new Cliente("2", "Cliente Teste");
        Veiculo veiculo = new Veiculo("XYZ-1987");

        UsoDeVaga usoDeVaga = new UsoDeVaga(vaga);
        LocalDateTime entrada = LocalDateTime.now().minusHours(2);
        LocalDateTime saida = LocalDateTime.now();

        veiculo.setCliente(cliente);
        usoDeVaga.usarVaga(veiculo, entrada);

        double valorCusto = usoDeVaga.calcularCusto(cliente);

        assertTrue(valorCusto > 0.0);
    }
}
