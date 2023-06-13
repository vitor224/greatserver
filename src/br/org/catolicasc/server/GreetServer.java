package br.org.catolicasc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        // inicializar atributos
        serverSocket = new ServerSocket(port);  // escuta na porta port
        System.out.println("Servidor iniciado. Aguardando conexão...");

        while (true) {
            clientSocket = serverSocket.accept();  // espera conexão
            System.out.println("Conexão estabelecida com o cliente.");

            // handler para escrita de dados
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            // handler para leitura de dados
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientHandler();

            // Verificar se o cliente enviou a mensagem de encerramento
            String quitMessage = in.readLine();
            if ("!quit".equals(quitMessage)) {
                System.out.println("Cliente encerrou a conexão.");
                break;
            }
        }
        stop();
    }

    private void clientHandler() throws IOException {
        String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
            out.println("hello client");
        } else {
            out.println("Mensagem incorreta.");
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public static void main(String[] args) {
        GreetServer server = new GreetServer();
        try {
            server.start(12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
