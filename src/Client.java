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
      String file_name = null;
      System.out.println("Type in name of music file and press enter.");
      System.out.println();

      while (true) {
        // Ждем пока пользователь введет что-то и нажмет кнопку Enter...
        file_name = keyboard.readLine();
        System.out.println("Sending this file name to the server...");
        out.writeUTF(file_name); // Отсылаем введенное название звукового файла серверу...
        out.flush(); // Заставляем поток закончить передачу данных...
        file_name = in.readUTF(); // Ждем ответа от сервера...
        System.out.println("Server sent : " + file_name);
        System.out.println("Enter more music file names.");
        System.out.println();
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
}