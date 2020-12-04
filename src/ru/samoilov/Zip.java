package ru.samoilov;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Класс для создания архива, содержащего аудиофайл...
class Zip{

  public static void main(String[] args) {

    // Путь исходного аудиофайла...
    String filePath = "MusicFile.wav";

    // Путь архива, в который запишем аудиофайл...
    String archivePath = "ZipSound/ZipFile.zip";

    // Создаем поток вывода для архива...
    // Создаем поток ввода для считывания аудиофайла...
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archivePath));
         FileInputStream fileInputStream= new FileInputStream("Sound/" + filePath);){

      // Добавляем в архив файл...
      ZipEntry zipEntry = new ZipEntry(filePath);
      zipOutputStream.putNextEntry(zipEntry);

      // Записываем звуковой файл в архив...
      byte[] buffer = new byte[fileInputStream.available()];
      fileInputStream.read(buffer);
      zipOutputStream.write(buffer);

      zipOutputStream.closeEntry();
    } catch (IOException e){
      e.printStackTrace();
    }

    System.out.println("Music file was added to archive");

  }
}