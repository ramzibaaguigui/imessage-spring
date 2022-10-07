package ramzanlabs.imessage.user.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
public class FileServingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServingController.class);
    private final FileServingService fileServingService;

    @Autowired
    public FileServingController(FileServingService fileServingService) {
        this.fileServingService = fileServingService;
    }

    @GetMapping("/user/profile/image")
    public ResponseEntity<?> getProfileImage(@RequestParam("image_identifier") String imageIdentifier) {

        try {
            File profileImageFile = fileServingService.getUserProfileImageFile(imageIdentifier);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
                    new FileInputStream(profileImageFile)
                            .readAllBytes()
            );
        } catch (FileNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {

            LOGGER.debug(exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
