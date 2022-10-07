package ramzanlabs.imessage.user.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.user.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class FileServingService {

    private final UserRepository userRepository;

    @Autowired
    public FileServingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public File getUserProfileImageFile(String imageIdentifier) throws FileNotFoundException, Exception {

        File file = Paths.get("uploads/profile").toFile();

        File[] files = file.listFiles();
        if (file == null) {
            throw new Exception("file not found");
        }
        System.out.println("the absolute path is: " + file.getAbsolutePath());
        System.out.println("the names of files:" + files);

        for (File ff: files) {
            System.out.println(ff.getName());
        }

        return Arrays.stream(file.listFiles())
                .filter(f -> f.getName().equals(imageIdentifier))
                .findAny().orElseThrow(FileNotFoundException::new);
    }
}
