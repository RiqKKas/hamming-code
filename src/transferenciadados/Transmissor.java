package transferenciadados;

import java.util.Random;

public class Transmissor {

    private final String mensagem;

    public Transmissor(String mensagem) {
        this.mensagem = mensagem;
    }

    private boolean[] streamCaracter(char simbolo) {

        //cada símbolo da tabela ASCII é representado com 8 bits
        boolean[] bits = new boolean[8];

        //convertendo um char para int (encontramos o valor do mesmo na tabela ASCII)
        int valorSimbolo = simbolo;
        int indice = 7;

        //convertendo cada "bits" do valor da tabela ASCII
        while (valorSimbolo >= 2) {
            int resto = valorSimbolo % 2;
            valorSimbolo /= 2;
            bits[indice] = (resto == 1);
            indice--;
        }
        
        bits[indice] = (valorSimbolo == 1);

        return bits;
    }

    private boolean[] dadoBitsHamming(boolean[] bits) {
        //criando um novo vetor de 12 posições para adicionar os bits de Hamming
        boolean[] codigoHamming = new boolean[12];
        
        int indiceBits = 0; //variável de controle para o vetor 'bits'
        
        //variáveis de controle para as posições dos bits de Hamming a serem adicionados
        int expoentePotenciaDois = 0;
        int indiceBitHamming = (int) (Math.pow(2, expoentePotenciaDois) - 1);

        //adicinando os valores contidos no vetor 'bits' nas devidas posições no vetor 'codigoHamming'
        for (int i = 0; i < 12; i++) {
            //pulando as posições onde os bits de Hamming devem ser adicionados
            if (i == indiceBitHamming) {
                expoentePotenciaDois++;
                indiceBitHamming = (int) (Math.pow(2, expoentePotenciaDois) - 1);
            } else {
                codigoHamming[i] = bits[indiceBits];
                indiceBits++;
            }
        }

        expoentePotenciaDois = 0;

        for (int j = 1; j <= 4; j++) {

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

            indiceBitHamming = (int) Math.pow(2, expoentePotenciaDois) - 1;
            expoentePotenciaDois++;

            codigoHamming[indiceBitHamming] = xor;
        }

        return codigoHamming;
    }

    private void geradorRuido(boolean[] bits) {
        Random geradorAleatorio = new Random();

        //pode gerar um erro ou não...
        if (geradorAleatorio.nextInt(5) > 1) {
            int indice = geradorAleatorio.nextInt(8);
            bits[indice] = !bits[indice];
        }
    }


    public void enviaDado(Receptor receptor) {
        for (int i = 0; i < this.mensagem.length(); i++) {
            boolean[] bits = streamCaracter(this.mensagem.charAt(i));

            //adicionando os bits de Hamming para contornar os problemas de ruidos
            bits = dadoBitsHamming(bits);

            //add ruidos na mensagem a ser enviada para o receptor
            geradorRuido(bits);

            //enviando a mensagem "pela rede" para o receptor
            receptor.receberDadoBits(bits);
        }
    }
}
