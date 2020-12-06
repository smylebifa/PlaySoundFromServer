package ru.samoilov;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.Socket;
import java.util.zip.ZipInputStream;

public class Client {

  public static void main(String[] args) throws Exception {

    // Создаем сокет используя IP-адрес и порт сервера...
    Socket socket = new Socket("localhost", 3000);

    // Местоположения звуковых файлов(сжатого и извлеченного)...
    String pathMusicFileZip = "ZipSound/ZipFileClient.zip";
    String pathMusicFile = "ZipSound/MusicFile.wav";

    try {

      // Создаем входной поток сокета для получения сжатого звукового файла.
      // Создаем поток для записи сжатого звукового файла.
      // Создаем потоки для распаковки и записи в файл...
      InputStream inputStream = socket.getInputStream();
      FileOutputStream fileOutputStream = new FileOutputStream(pathMusicFileZip);
      ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(pathMusicFileZip));
      FileOutputStream fileOutputStreamFile = new FileOutputStream(pathMusicFile);
      DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

      while (true) {
        // Создаем поток для чтения с клавиатуры...
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Get to get music file or Close to close program.");

        String commandName = keyboard.readLine();

        if (commandName.equals("Close") || commandName.equals("close")) break;

        System.out.println("Sending client command to the server...");
        dataOutputStream.writeUTF(commandName); // Отсылаем введеную команду серверу...
        dataOutputStream.flush(); // Заставляем поток закончить передачу данных...
        

        // Получение сжатого звукового файла с сервера...
        System.out.println("Transferring compressed file");

        int c = 0;
        while ((c = inputStream.read()) >= 0) {
          fileOutputStream.write((char) c);
        }

        System.out.println("Compressed file was transferred");

        // Закрываем поток...
        fileOutputStream.close();


        // Получаем сжатый файл для считывания...
        zipInputStream.getNextEntry();

        // Распаковываем архив и записываем в звуковой файл...
        for (c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
          fileOutputStreamFile.write(c);
        }

        // Закрываем потоки...
        fileOutputStreamFile.close();
        zipInputStream.closeEntry();
        zipInputStream.close();

        System.out.println("Compressed file was saved");


        // Получаем поток для воспроизведения записанного файла...
        File file = new File(pathMusicFile);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // Воспроизводим звук из выбранного файла...
        System.out.println("Playing sound");

        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

        // Ждем завершения воспроизведения аудиофайла...
        Thread.sleep(3000);
        clip.stop();
        clip.close();

        System.out.println();
      }
    } finally {
      System.out.println("closing...");
      socket.close();
    }
  }
}