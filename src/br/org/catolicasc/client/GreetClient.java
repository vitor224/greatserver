package br.org.catolicasc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GreetClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        // handler para escrita de dados
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // handler lara leitura de dados
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Conexão estabelecida com o servidor.");

        // Manter a conexão até receber uma mensagem tipo "!quit"
        while (true) {
            // Ler a entrada do usuário com Scanner
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite uma mensagem: ");
            String message = scanner.nextLine();
            out.println(message);  // enviar a mensagem para o servidor

            // Verificar se o cliente enviou a mensagem de encerramento
            if ("!quit".equals(message)) {
                System.out.println("Conexão encerrada pelo cliente.");
                break;
            }

            // Receber a resposta do servidor
            String response = in.readLine();
            System.out.println("Resposta do servidor: " + response);
        }

        stop();
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public static void main(String[] args) {
        GreetClient client = new GreetClient();
        try {
            client.start("127.0.0.1", 12345);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
