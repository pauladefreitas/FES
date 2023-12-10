import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ClienteTest {

    private Cliente cliente;
    private Veiculo veiculo1, veiculo2;

    @Before
    public void setUp() {
        cliente = new Cliente("João Silva", "123");
        cliente.setModalidade(Cliente.ModalidadeCliente.HORISTA);
        veiculo1 = new Veiculo("ABC1234");
        veiculo2 = new Veiculo("XYZ5678");

        // Adicionando veículos ao cliente
        cliente.addVeiculo(veiculo1);
        cliente.addVeiculo(veiculo2);

        // Criando vaga
        Vaga vaga1 = new Vaga("Vaga1");
        Vaga vaga2 = new Vaga("Vaga2");

        // Simulando usos dos veículos
        LocalDateTime entrada1 = LocalDateTime.of(2023, 4, 1, 8, 0);
        LocalDateTime saida1 = LocalDateTime.of(2023, 4, 1, 10, 0);
        veiculo1.setCliente(cliente);
        veiculo1.estacionar(vaga1, entrada1);
        veiculo1.sair(saida1);

        LocalDateTime entrada2 = LocalDateTime.of(2023, 4, 2, 8, 0);
        LocalDateTime saida2 = LocalDateTime.of(2023, 4, 2, 10, 0);
        veiculo2.setCliente(cliente);
        veiculo2.estacionar(vaga2, entrada2);
        veiculo2.sair(saida2);
    }

    @Test
    public void testAddVeiculo() {
        assertEquals(2, cliente.getVeiculos().size());
    }

    @Test
    public void testPossuiVeiculo() {
        assertNotNull(cliente.possuiVeiculo("ABC1234"));
        assertNull(cliente.possuiVeiculo("INEXISTENTE"));
    }

    @Test
    public void testTotalDeUsos() {
        assertEquals(2, cliente.totalDeUsos());
    }

    @Test
    public void testArrecadadoPorVeiculo() {
        double valorEsperado1 = veiculo1.totalArrecadado();
        assertEquals(valorEsperado1, cliente.arrecadadoPorVeiculo("ABC1234"), 0.01);

        double valorEsperado2 = veiculo2.totalArrecadado();
        assertEquals(valorEsperado2, cliente.arrecadadoPorVeiculo("XYZ5678"), 0.01);
    }

    @Test
    public void testArrecadadoTotal() {
        double valorEsperado = veiculo1.totalArrecadado() + veiculo2.totalArrecadado();
        assertEquals(valorEsperado, cliente.arrecadadoTotal(), 0.01);
    }

    @Test
    public void testArrecadadoNoMes() {
        double valorEsperado = veiculo1.arrecadadoNoMes(4) + veiculo2.arrecadadoNoMes(4);
        assertEquals(valorEsperado, cliente.arrecadadoNoMes(4), 0.01);
    }
}