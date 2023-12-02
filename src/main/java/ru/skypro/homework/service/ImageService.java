package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    void updateUserImage(UserEntity user, MultipartFile image, Path filePath);
    PhotoEntity updateAdImage(AdEntity ad, MultipartFile image, Path filePath) throws IOException;

    boolean saveFileOnDisk(MultipartFile image, Path filePath) throws IOException;

    String getExtension(String fileName);
}
