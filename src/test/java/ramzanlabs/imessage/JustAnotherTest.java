package ramzanlabs.imessage;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ramzanlabs.imessage.user.Constants;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class JustAnotherTest {

    @Test
    public void test() {
        Path folder = Paths.get(Constants.PATH_TO_IMAGE_FOLDER);
        if (folder.toFile().exists()) {
            System.out.println("the file is already exisint");
            return;
        }

        System.out.println("the file does not exist");
        System.out.println("attempting to create file...");
        boolean created = folder.toFile().mkdir();
        System.out.println("the file is create: " + created);

    }
}
