import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args){
    int port = 3000;
    try {
      // Создаем сокет сервера и привязываем его к вышеуказанному порту...
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Waiting for a client...");

      // Заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером...
      Socket socket = serverSocket.accept();
      System.out.println("Got a client.");
      System.out.println();

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту...
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения...
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      while(true) {
        String file_name = in.readUTF();
        File file = new File("Sound/" + file_name + ".wav");

        if (file.exists()) {
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);

          // Воспроизводим звук из выбранного файла...
          Clip clip = AudioSystem.getClip();
          clip.open(inputStream);
          clip.start();

          // Ждем заверщения воспроизведения аудиофайла...
          Thread.sleep(3000);
          clip.stop();
          clip.close();

          // Отправляем ответ клиенту о завершении воспроизведения аудиофайла...
          out.writeUTF("Sound was played");
        }

        else out.writeUTF("This music file not exist");

        out.flush(); // Заставляем поток закончить передачу данных...
        System.out.println("Waiting for the next music file name...");
        System.out.println();
      }
    } catch(Exception e) { e.printStackTrace(); }
  }
}