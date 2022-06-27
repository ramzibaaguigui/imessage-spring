package ramzanlabs.imessage.file;


import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {
    private final Path uploads = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile uploadedFile, HttpServletRequest request) throws IOException {
        try {

            File createdFile = new File(uploads + "/" + RandomString.make(45));
            System.out.println("before create new file");
            boolean fileCreated = createdFile.createNewFile();
            System.out.println("after create new file");
            if (fileCreated) {
                FileOutputStream fileOutputStream = new FileOutputStream(createdFile);
                fileOutputStream.write(uploadedFile.getBytes());
                fileOutputStream.close();
                return ResponseEntity.ok("File uploaded successfully");
            } else {
                System.out.println("File could not be created");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("This is an exception handling");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("THERE has been some error");

    }

    /**
     * Just writing some staff about the createNewFile method
     * Basically, the method attemps to create new file with the information specified(path and name)
     * If the file is already existing it
     */

    /**
     * Note that there is some bean resolver which is needed
     * I have copied the code below from some website
     * We are needing to declare a CommonsMultipartResolver
     * After this, everthing is goingto be working as expected
     *
     */



}
