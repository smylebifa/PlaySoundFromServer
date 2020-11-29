package ru.samoilov;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.*;

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

      // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту...
      InputStream sin = socket.getInputStream();
      OutputStream sout = socket.getOutputStream();

      // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения...
      DataInputStream in = new DataInputStream(sin);
      DataOutputStream out = new DataOutputStream(sout);

      while(true) {

        // Название полученной команды...
        String commandName = in.readUTF();

        // Если введена команда верно, то получаем из архива и воспроизводим звуковой файл...
        if(commandName.equals("Play") || commandName.equals("play")){

          // Получаем архив для распоковки...
          ZipInputStream zin = new ZipInputStream(new FileInputStream("ZipSound/test.zip"));
          ZipEntry entry;

          entry = zin.getNextEntry();
          String name = entry.getName();

          // Распаковка...
          FileOutputStream fout = new FileOutputStream("ZipSound/" + name);
          for (int c = zin.read(); c != -1; c = zin.read()) {
            fout.write(c);
          }
          fout.flush();
          zin.closeEntry();
          fout.close();
          zin.close();

          File file = new File("ZipSound/" + name);

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

        else System.out.println("Wrong command");

        System.out.println("Waiting for the next entering command...");

        out.flush(); // Заставляем поток закончить передачу данных...
      }
    } catch(Exception e) { e.printStackTrace(); }
  }
}


// Класс для создания архива, содержащего аудиофайл...
class Zip{

  public static void main(String[] args) {

    // Название исходного аудиофайла...
    String fileName = "MusicFile.wav";

    // Создаем поток вывода для архива...
    // Создаем поток ввода для считывания аудиофайла...
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("ZipSound/test.zip"));
         FileInputStream fileInputStream= new FileInputStream("Sound/" + fileName);){

      // Добавляем в архив файл...
      ZipEntry zipEntry = new ZipEntry(fileName);
      zipOutputStream.putNextEntry(zipEntry);

      // Записываем звуковой файл в архив...
      byte[] buffer = new byte[fileInputStream.available()];
      fileInputStream.read(buffer);
      zipOutputStream.write(buffer);

      zipOutputStream.closeEntry();
      zipOutputStream.close();
    } catch (IOException e){
      e.printStackTrace();
    }

    System.out.println("Music file was added to archive");

  }
}
