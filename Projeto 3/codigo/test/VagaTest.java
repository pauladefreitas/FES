import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VagaTest {
    private Vaga vaga;

    @Before
    public void setUp() {
        vaga = new Vaga("V1");
    }

    @Test
    public void testEstacionarVagaDisponivel() {
        assertTrue(vaga.estacionar());
    }

    @Test
    public void testEstacionarVagaOcupada() {
        vaga.estacionar();
        assertFalse(vaga.estacionar());
    }

    @Test
    public void testSairVagaOcupada() {
        vaga.estacionar();
        assertTrue(vaga.sair());
    }

    @Test
    public void testSairVagaDisponivel() {
        assertTrue(vaga.disponivel());
        assertFalse(vaga.sair());
    }
}
