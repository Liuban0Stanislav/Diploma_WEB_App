package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.PhotoOnDatabaseIsAbsentException;
import ru.skypro.homework.exception.PhotoOnPcIsAbsentException;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.PhotoService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final ImageServiceImpl imageService;

    public PhotoServiceImpl(PhotoRepository photoRepository, ImageServiceImpl imageService) {
        this.photoRepository = photoRepository;
        this.imageService = imageService;
    }

    /**
     * Метод возвращает фото с ПК, а если его там нет по каким-то причинам,
     * то перенаправляет запрос фото в базу данных.
     * @param photoId
     * @return byte[] массив байт
     * @throws IOException
     */
    public byte[] getPhoto(Integer photoId) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        log.info("photoId: {}", photoId);

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
