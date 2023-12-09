package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.ModelEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final PhotoRepository photoRepository;
    private final UserMapper userMapper;

    @Value("${path.to.photos.folder}")
    private String photoDir;


    public ImageServiceImpl(PhotoRepository photoRepository, UserMapper userMapper) {
        this.photoRepository = photoRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ModelEntity updateEntitiesPhoto(MultipartFile image, ModelEntity entity) throws IOException {
        //если у сущности уже есть картинка, то нужно ее удалить
        if (entity.getPhoto() != null) {
            photoRepository.delete(entity.getPhoto());
        }

        //заполняем поля photo и сохраняем фото в БД
                PhotoEntity photoEntity = userMapper.mapMuptipartFileToPhoto(image);
        log.info("Создана сущность photoEntity - {}", photoEntity != null);
        entity.setPhoto(photoEntity);
        photoRepository.save(photoEntity);

        //адрес до директории хранения фото на ПК
        Path filePath = Path.of(photoDir, entity.getPhoto().getId() + "."
                + this.getExtension(image.getOriginalFilename()));
        log.info("путь к файлу для хранения фото на ПК: {}", filePath);

        //добавляем в сущность фото путь где оно хранится на ПК
        entity.getPhoto().setFilePath(filePath.toString());

        //добавляем в сущность путь на ПК
        entity.setFilePath(filePath.toString());

        //сохранение на ПК
        this.saveFileOnDisk(image, filePath);

        return entity;
    }


    /**
     * Метод сохраняет изображение на диск
     *
     * @param image    - изображение
     * @param filePath - путь, куда будет сохранено изображение
     * @return boolean
     * @throws IOException
     */
    @Override
    public boolean saveFileOnDisk(MultipartFile image, Path filePath) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        return true;
    }

    public byte[] getPhotoFromDisk(PhotoEntity photo) {
        Path path1 = Path.of(photo.getFilePath());
        try {
            return Files.readAllBytes(path1);
        } catch (IOException e) {
            throw new NoSuchFieldException("Искомый файл аватара или фото объявления, отсутствует на ПК\n" +
                    "Поиск файла перенаправлен в БД");
        } finally {
            return null;
        }
    }

    /**
     * Метод получает расширение изображения
     *
     * @param fileName - полное название изображения
     * @return расширение изображения
     */
    @Override
    public String getExtension(String fileName) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
