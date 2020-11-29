package ru.samoilov;

import java.io.*;
import java.net.Socket;

public class Client{
  public static void main(String[] args){
    int serverPort = 3000; // Порт к которому привязывается сервер...
    try {
      // Создаем сокет используя IP-адрес и порт сервера...
      Socket socket = new Socket("localhost", serverPort);

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом...
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения...
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      // Создаем поток для чтения с клавиатуры...
      BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
      String commandName = null;
      System.out.println("Type in Play and press enter.");
      System.out.println();

      while (true) {
        // Ждем пока пользователь введет команду Play и нажмет кнопку Enter...
        commandName = keyboard.readLine();
        System.out.println("Sending client message to the server...");
        out.writeUTF(commandName); // Отсылаем введеное сообщение серверу...
        out.flush(); // Заставляем поток закончить передачу данных...
        String messageFromServer = in.readUTF(); // Ждем ответа от сервера...
        System.out.println("Server answer : " + messageFromServer);
        System.out.println("Enter Play once more.");
        System.out.println();
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
}