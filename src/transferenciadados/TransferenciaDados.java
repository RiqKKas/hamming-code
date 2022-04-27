package transferenciadados;

import java.util.Scanner;

public class TransferenciaDados {

    public static void main(String[] args) {
        
        Scanner entrada = new Scanner(System.in);
        
        System.out.print("Informe a informação a ser transmitida: ");

        //criando receptor e transmissor com a informação a ser enviada
        Transmissor seuSmartphone = new Transmissor(entrada.nextLine());
        Receptor umServidorQualquer = new Receptor();
        
        //"enviando" a informação do "smartphone" ao "servidor"
        seuSmartphone.enviaDado(umServidorQualquer);
     
        //imprimindo a mensagem recebida no "servidor"
        System.out.println("Informação recebida: " + umServidorQualquer.getMensagem());
    }
}
