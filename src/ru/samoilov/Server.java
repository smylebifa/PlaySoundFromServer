package ru.samoilov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public static void main(String[] args) {

    try {
      // Создаем сокет сервера и привязываем его к вышеуказанному порту...
      ServerSocket serverSocket = new ServerSocket(3000);
      System.out.println("Waiting for a client...");

      // Заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером...
      Socket socket = serverSocket.accept();
      System.out.println("Got a client.");

      // Создаем входной поток для получения сжатого звукового файла
      // и выходной поток сокета для отправки данных клиенту...
      try (InputStream inputStream = new FileInputStream("ZipSound/ZipFile.zip");
           OutputStream outputStream = socket.getOutputStream()) {

        // Передаем архив со звуковым файлом клиенту...
        System.out.println("Transferring compressed file");

        int c = 0;
        while((c = inputStream.read()) >= 0) {
          outputStream.write(c);
          outputStream.flush();
        }

        System.out.println("File was transferred");
      }

      // Закрываем сокет клиента и сервера...
      socket.close();
      serverSocket.close();

    } catch (IOException e) { e.printStackTrace(); }
  }
}