package ramzanlabs.imessage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ramzanlabs.imessage.user.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class FileTest {

    private final Path root = Paths.get("uploads");
    @Test
    public void testFile() throws IOException {
        if (root.toFile().exists()){

            System.out.println("File already exists");
            // Files.createDirectory(root.resolve("child"));
            System.out.println("file created");
            File file = new File(root.resolve("file.txt").toString());
            file.renameTo(new File("file.txt"));
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("hello world this is ietoi ");
            fileWriter.close();
            System.out.println(file.exists());
        } else {
            Files.createDirectory(root);
        }


    }

}
