import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileIO {
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { //Throws IOException with a customized message if there is a problem with the input file.
            throw new IOException("ERROR: This program cannot read from the \"" + path + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
        }
    }

    public static int writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (IOException e) {
            return -1;
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
        return 0;
    }
}

