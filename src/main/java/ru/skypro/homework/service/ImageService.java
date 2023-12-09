package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.ModelEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

//    void updateUserImage(UserEntity user, MultipartFile image, Path filePath);
//    PhotoEntity updateAdImage(Integer id, MultipartFile image, Path filePath) throws IOException;
    ModelEntity updateEntitiesPhoto(MultipartFile image, ModelEntity entity) throws IOException;
    boolean saveFileOnDisk(MultipartFile image, Path filePath) throws IOException;
    byte[] getPhotoFromDisk(PhotoEntity photo) throws IOException;
    String getExtension(String fileName);
}
