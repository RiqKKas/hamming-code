package transferenciadados;

public class Receptor {

    //mensagem recebida pelo transmissor
    private String mensagem;

    public Receptor() {
        //mensagem vazia no inicio da execução
        this.mensagem = "";
    }

    public String getMensagem() {
        return mensagem;
    }

    private void decodificarDado(boolean[] bits) {
        int codigoAscii = 0;
        int expoente = bits.length - 1;

        //converntendo os "bits" para valor inteiro para então encontrar o valor tabela ASCII
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                codigoAscii += Math.pow(2, expoente);
            }
            expoente--;
        }

        //concatenando cada simbolo na mensagem original
        this.mensagem += (char) codigoAscii;
    }

    private boolean[] decoficarDadoHamming(boolean[] codigoHamming) {
        //variável de controle para posição, em binario,  do possível bit com ruido
        String indiceRuidoBin = "";

        for (int j = 4; j >= 1; j--) {

            boolean xor = false;

            //iteração sobre as posições que possuem bits significativos relevantes para cada posição de bit de Hamming
            for (int i = 0; i < 12; i++) {
                String indiceBinario = Integer.toBinaryString(i + 1);
                int controleDeIndice = indiceBinario.length() - j;
                char bitSignificativo = controleDeIndice >= 0 ? indiceBinario.charAt(controleDeIndice) : 'x';

                //se a posição for relevante, o 'ou exclusivo' será executado e seu resultado guardado em 'xor'
                if (bitSignificativo == '1') {
                    xor = !(codigoHamming[i] == xor);
                }
            }

            //adicionado o resultado da operação xor para montar o indice do possivel bit com ruido
            indiceRuidoBin += xor ? "1" : "0";
        }

        //concerta o bit com ruido, se o mesmo existir
        if (indiceRuidoBin.contains("1")) {
            int indiceRuidoInt = Integer.parseInt(indiceRuidoBin, 2) - 1;
            codigoHamming[indiceRuidoInt] = !codigoHamming[indiceRuidoInt];
        }
        
        //novo vetor para os bits da msg menos os bits de Hamming
        boolean[] bits = new boolean[8];

        int indiceBits = 0; //variável de controle para o vetor 'bits'
        
        //variáveis de controle para as posições dos bits de Hamming a serem retirados
        int expoentePotenciaDois = 0;
        int indiceBitHamming = (int) (Math.pow(2, expoentePotenciaDois) - 1);

        for (int i = 0; i < 12; i++) {
            //pulando as posições onde os bits de Hamming estão
            if (i == indiceBitHamming) {
                expoentePotenciaDois++;
                indiceBitHamming = (int) (Math.pow(2, expoentePotenciaDois) - 1);
            } else {
                bits[indiceBits] = codigoHamming[i];
                indiceBits++;
            }
        }

        return bits;
    }

    public void receberDadoBits(boolean[] bits) {
        //verificando se a msg recebida possuí ruido e, caso necessário, corrigindo-a
        bits = decoficarDadoHamming(bits);
        
        //decodificando a msg de acordo com a tabela ASCII
        decodificarDado(bits);
    }
}
