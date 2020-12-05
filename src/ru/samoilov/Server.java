package ru.samoilov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public static void main(String[] args) throws IOException {

    // Создаем сокет сервера и привязываем его к вышеуказанному порту...
    ServerSocket serverSocket = new ServerSocket(3000);
    System.out.println("Waiting for a client...");

    try {

      // Заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером...
      Socket socket = serverSocket.accept();
      System.out.println("Got a client.");

      try {
        // Создаем входной поток для получения сжатого звукового файла
        // и выходной поток сокета для отправки данных клиенту...
        InputStream inputStream = new FileInputStream("ZipSound/ZipFile.zip");
        OutputStream outputStream = socket.getOutputStream();
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        String commandName = dataInputStream.readUTF();

        // Если введена команда верно, то получаем из архива и воспроизводим звуковой файл...
        if (commandName.equals("Get") || commandName.equals("get")) {

          // Передаем архив со звуковым файлом клиенту...
          System.out.println("Transferring compressed file");

          int c = 0;
          while ((c = inputStream.read()) >= 0) {
            outputStream.write(c);
            outputStream.flush();
          }

          System.out.println("File was transferred");
        }
      }
      finally {
        System.out.println("closing...");
        socket.close();
      }
    }
    finally {
      serverSocket.close();
    }
  }
}