package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.PhotoOnDatabaseIsAbsentException;
import ru.skypro.homework.exception.PhotoOnPcIsAbsentException;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final ImageServiceImpl imageService;
    private final UserServiceImpl userService;

    public PhotoServiceImpl(PhotoRepository photoRepository, ImageServiceImpl imageService, UserServiceImpl userService) {
        this.photoRepository = photoRepository;
        this.imageService = imageService;
        this.userService = userService;
    }

    /**
     * Метод возвращает фото с ПК, а если его там нет по каким-то причинам,
     * то перенаправляет запрос на получение фото в базу данных.
     *
     * @param photoId
     * @return byte[] массив байт
     * @throws IOException
     */
    public byte[] getPhoto(Integer photoId) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        log.info("photoId: {}", photoId);

        //получаем фото из БД
        PhotoEntity photo = photoRepository.findById(photoId).orElseThrow(PhotoOnDatabaseIsAbsentException::new);
        log.info("Фото найдено - {}", photo.getData() != null);

        //Если картинка запрошенная с ПК не получена по какой-то причине, достаем ее из БД
        if (imageService.getPhotoFromDisk(photo) == null) {
            return photoRepository.findById(photoId).orElseThrow(PhotoOnPcIsAbsentException::new).getData();
        }
        //Если предыдущее условие не выполнилось и с картинкой все в порядке, то достаем ее с ПК
        return imageService.getPhotoFromDisk(photo);
    }

}
