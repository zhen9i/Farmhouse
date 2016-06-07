package game;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileSaver {

  public  final static String DIR_PATH_SAVE = "save/";
  public final static String DIR_PATH_STAT = "stat/";
  public final static String NOT_PATH_STAT = "notation/";
  public final static String EXT_FILE_DAT = ".dat";
  public final static String TXT_FILE_NOT = ".txt";

  public static String generateSaveFile(String dirPath, String extansion) {
    Path dir = FileSystems.getDefault().getPath(dirPath);
    LinkedList<String> path = new LinkedList<>();
    int index = 0;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*" + extansion)) {
      for (Path file : stream) {
        path.addLast(file.getFileName().toString());
      }
    } catch (IOException | DirectoryIteratorException x) {
      System.err.println(x);
    }
    if (!path.isEmpty()) {
      index = Integer.parseInt(path.getLast().replaceAll(extansion, ""));
    }
    return dirPath + String.valueOf(++index) + extansion;
  }

  public static String getLastSaveFile() {
    Path dir = FileSystems.getDefault().getPath(DIR_PATH_SAVE);
    LinkedList<String> path = new LinkedList<>();
    try (DirectoryStream<Path> stream =
        Files.newDirectoryStream(dir, "*" + EXT_FILE_DAT)) {
      for (Path file : stream) {
        file.toFile().length();
        path.addLast(file.getFileName().toString());
      }
    } catch (IOException | DirectoryIteratorException x) {
      System.err.println(x);
    }
    if (!path.isEmpty()) {
      return DIR_PATH_SAVE + path.getLast();
    }
    return "";
  }
  
  public static ArrayList<String> getStatisticFile() {
    Path dir = FileSystems.getDefault().getPath(DIR_PATH_STAT);
    ArrayList<String> path = new ArrayList<>();
    try (DirectoryStream<Path> stream =
        Files.newDirectoryStream(dir, "*" + EXT_FILE_DAT)) {
      for (Path file : stream) {
        file.toFile().length();
        path.add(DIR_PATH_STAT + file.getFileName().toString());
      }
    } catch (IOException | DirectoryIteratorException x) {
      System.err.println(x);
    }
    return path;
  }

  public static String getSaveWithSort(boolean java, boolean first) {
    Path dir = FileSystems.getDefault().getPath(DIR_PATH_SAVE);
    ArrayList<ReplayInfo> replaiInfo = new ArrayList<>();
    try (DirectoryStream<Path> stream =
        Files.newDirectoryStream(dir, "*" + EXT_FILE_DAT)) {
      for (Path file : stream) {
        replaiInfo.add(new ReplayInfo(file.getFileName().toString(),
            file.toFile().length()));
      }
    } catch (IOException | DirectoryIteratorException x) {
      System.err.println(x);
    }
    ReplayInfo[] array = new ReplayInfo[replaiInfo.size()];
    for (int i = 0; i < replaiInfo.size(); i++) {
      array[i] = replaiInfo.get(i);
    }
    if (java) {
      array = JavaSort.quickSort(array, replaiInfo.size());
    } else {
      new Algorithm().sort(array);
    }
    return DIR_PATH_SAVE + (first ? array[0].getPath()
        : array[replaiInfo.size() - 1].getPath());
  }

  static class ReplayInfo {
    private String path = null;
    private long lenght = 0;

    public ReplayInfo(String aPath, long aLenght) {
      path = aPath;
      lenght = aLenght;
    }

    public String getPath() {
      return path;
    }

    public long getLenght() {
      return lenght;
    }
  }
}
