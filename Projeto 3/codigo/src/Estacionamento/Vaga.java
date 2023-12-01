/**
 * Classe que representa uma vaga de estacionamento.
 */
public class Vaga {

    private String id;
    private boolean disponivel;

    /**
     * Construtor que cria uma nova vaga com um identificador especificado.
     *
     * @param id O identificador da vaga.
     */
    public Vaga(String id) {
        this.id = id;
        this.disponivel = true;
    }

    /**
     * Estaciona um veículo na vaga, marcando-a como não disponível.
     *
     * @return true se o veículo foi estacionado com sucesso na vaga, false se a vaga já estiver ocupada.
     */
    public boolean estacionar() {
        if (disponivel) {
            this.disponivel = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove um veículo da vaga, marcando-a como disponível.
     *
     * @return true se o veículo saiu da vaga com sucesso, false se a vaga já estiver disponível.
     */
    public boolean sair() {
        if (!disponivel) {
            this.disponivel = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se a vaga está disponível para estacionamento.
     *
     * @return true se a vaga está disponível, false se a vaga já está ocupada.
     */
    public boolean disponivel() {
        return disponivel;
    }

    /**
     * Obtém o identificador da vaga.
     *
     * @return O identificador da vaga.
     */
    public String getId() {
        return id;
    }
}
