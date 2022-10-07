package ramzanlabs.imessage.user.file;


import ch.qos.logback.core.util.FileSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserService;
import ramzanlabs.imessage.user.auth.TokenGenerator;
import ramzanlabs.imessage.user.exception.CannotCreateImageFolderException;
import ramzanlabs.imessage.user.exception.FileTypeNotImageException;
import ramzanlabs.imessage.user.exception.MaximumImageSizeExceededException;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageUploadService {

    private final TokenGenerator fileNameGenerator;
    private static final String PATH_PROFILE_IMAGE = "uploads/profile";
    private static final Path PROFILE_IMAGE_UPLOAD_PATH = Paths.get(PATH_PROFILE_IMAGE);
    private final UserService userService;

    @Autowired
    public ImageUploadService(TokenGenerator fileNameGenerator, UserService userService) {
        this.fileNameGenerator = fileNameGenerator;
        this.userService = userService;
    }

    public User updateProfileImage(User currentUser, MultipartFile uploadedImageMultipartFile)
            throws MaximumImageSizeExceededException, FileTypeNotImageException, IOException,
            CannotCreateImageFolderException {
        if (!fileIsSupportedImage(uploadedImageMultipartFile)) {
            throw new FileTypeNotImageException("The uploaded file is not an image");
        }

        if (fileExceedsMaximumSize(uploadedImageMultipartFile)) {
            throw new MaximumImageSizeExceededException("the file size is higher than the maximum");
        }

        createProfileImageDirectoryIfNotExisting();


        String generatedFileName = generateRandomNameForProfileImage();
        Files.copy(uploadedImageMultipartFile.getInputStream(),
                PROFILE_IMAGE_UPLOAD_PATH.resolve(generatedFileName));

        currentUser = userService.updateUserProfileUrl(currentUser, generatedFileName);
        return currentUser;
    }

    private boolean fileIsSupportedImage(MultipartFile multipartFile) {
        return true;
    }

    private boolean fileExceedsMaximumSize(MultipartFile multipartFile) {
        return false;
    }

    public void profileDirectoryExists(String directoryPath) {


        Path path;
    }

    private boolean profileImageDirectoryExists() {
        return Files.exists(Paths.get(PATH_PROFILE_IMAGE));
    }

    private void createProfileImageDirectory() throws CannotCreateImageFolderException {
        File folder = new File(PATH_PROFILE_IMAGE);
        boolean created = folder.mkdirs();
        if (!created) {
            throw new CannotCreateImageFolderException("the image folder cannot be creted");
        }
    }

    private String generateRandomNameForProfileImage() {
        String generatedName = fileNameGenerator.generateRandomToken();
        System.out.println("the generated name is: " + generatedName);
        return generatedName;
    }

    private void createProfileImageDirectoryIfNotExisting() {
        if (profileImageDirectoryExists()) {
            return;
        }

        if (Files.exists(Paths.get(PATH_PROFILE_IMAGE))) {
            System.out.println("the file exists already");
            return;

        }
        File file = new File(PATH_PROFILE_IMAGE);
        file.mkdir();

        System.out.println("the folder is created successfully");

    }
}
