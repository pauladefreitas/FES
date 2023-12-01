    import java.time.Duration;
    
    /**
     * Enumeração que define os serviços adicionais disponíveis para um uso de vaga.
     */
    public enum Servico {
        MANOBRISTA("Manobrista", 5.0, Duration.ZERO),
        LAVAGEM("Lavagem", 20.0, Duration.ofHours(1)),
        POLIMENTO("Polimento (inclui lavagem)", 45.0, Duration.ofHours(2));

        private final String descricao;
        private final double valor;
        private final Duration tempoMinimo;

        Servico(String descricao, double valor, Duration tempoMinimo) {
            this.descricao = descricao;
            this.valor = valor;
            this.tempoMinimo = tempoMinimo;
        }

        /**
         * Obtém a descrição do serviço.
         *
         * @return A descrição do serviço.
         */
        public String getDescricao() {
            return descricao;
        }

        /**
         * Obtém o valor do serviço.
         *
         * @return O valor do serviço.
         */
        public double getValor() {
            return this.valor;
        }

        /**
         * Obtém o tempo mínimo necessário para o serviço.
         *
         * @return O tempo mínimo necessário para o serviço.
         */
        public Duration getTempoMinimo() {
            return tempoMinimo;
        }
    }
