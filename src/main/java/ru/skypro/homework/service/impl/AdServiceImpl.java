package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
//@AllArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final PhotoRepository photoRepository;
    private final AdMapper adMapper;
    private final ImageServiceImpl imageService;
    private final UserServiceImpl userService;
    @Value("${path.to.photos.folder}")
    private String photoDir;

    public AdServiceImpl(AdRepository adRepository,
                         PhotoRepository photoRepository,
                         AdMapper adMapper,
                         ImageServiceImpl imageService,
                         UserServiceImpl userService) {
        this.adRepository = adRepository;
        this.photoRepository = photoRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
        this.userService = userService;
    }

    /**
     * Метод возвращает список всех объявлений в виде DTO {@link Ad}.
     *
     * @return возвращает все объявления из БД
     */

    @Override
    public Ads getAllAds() {
        List<Ad> dtos = adRepository.findAll().stream()
                .map(entity -> adMapper.mapToAdDto(entity))
                .collect(Collectors.toList());
        return new Ads(dtos.size(), dtos);
    }

    /**
     * Метод добавляет новое объявление в БД
     *
     * @param properties - DTO модель класса {@link CreateOrUpdateAd};
     * @param image      - фотография объявления
     * @return возвращает объявление в качестве DTO модели
     */

    @Override
    public Ad addAd(CreateOrUpdateAd properties,
                    MultipartFile image,
                    Authentication authentication) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        //создаем сущность
        AdEntity adEntity = new AdEntity();

        //заполняем поля title, price и description, которые берутся из properties
        adEntity.setTitle(properties.getTitle());
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());

        //заполняем поле author
        adEntity.setAuthor(userService.getUser(authentication.getName()));
        //заполняем поля
        adEntity = (AdEntity) imageService.updateEntitiesPhoto(image, adEntity);
        log.info("Сущность adEntity сформированная в {}", LoggingMethodImpl.getMethodName());

        //сохранение сущности adEntity в БД
        adRepository.save(adEntity);

        //возврат ДТО Ad из метода
        return adMapper.mapToAdDto(adEntity);
    }


    /**
     * Метод получает информацию об объявлении по id
     *
     * @param id - id объявления
     * @return возвращает DTO модель объявления
     */
    @Override
    public ExtendedAd getAds(Integer id) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        AdEntity entity = adRepository.findById(id).get();
        return adMapper.mapToExtendedAdDto(entity);
    }

    /**
     * Метод удаляет объявление по id
     *
     * @param id - id объявления
     * @return boolean
     */
    @Transactional
    @Override
    public boolean removeAd(Integer id) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        AdEntity ad = adRepository.findById(id).get();
        if (ad != null) {
            //удаление объявления из БД
            adRepository.delete(ad);
            //удаление фото из БД
            photoRepository.delete(ad.getPhoto());
            //получение пути из сущности объявления
            String filePath = ad.getFilePath();
            //создание пути к файлу
            Path path = Path.of(filePath);
            //удаление файла с ПК
            Files.delete(path);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод изменяет объявление
     *
     * @param id  - id объявления
     * @param dto - DTO модель класса {@link CreateOrUpdateAd};
     * @return возвращает DTO модель объявления
     */
    @Transactional
    @Override
    public Ad updateAds(Integer id, CreateOrUpdateAd dto) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        AdEntity entity = adRepository.findById(id).get();

        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());

        adRepository.save(entity);
        return adMapper.mapToAdDto(entity);
    }

    /**
     * Метод получает все объявления данного пользователя
     *
     * @param username - логин пользователя
     * @return возвращает DTO - список моделей объявления пользователя
     */
    @Override
    @Transactional
    public Ads getAdsMe(String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        UserEntity author = userService.getUser(username);
        log.info("объект UserEntity получен из БД");
        List<Ad> ads = null;
        ads = adRepository.findByAuthor(author).stream()
                .map(ad -> adMapper.mapToAdDto(ad))
                .collect(Collectors.toList());
        log.info("Получен список объявлений пользователя ads");
        Ads adsDto = new Ads(ads.size(), ads);
        log.info("Сформирован возвращаемый объект adsDto");
        return adsDto;
    }

    @Transactional
    @Override
    public void updateImage(Integer id, MultipartFile image) throws IOException {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        //достаем объявление из БД
        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        //заполняю поля и получаю сущность в переменную
        adEntity = (AdEntity) imageService.updateEntitiesPhoto(image, adEntity);
        log.info("adEntity cоздано - {}", adEntity != null);
        //сохранение сущности user в БД
        adRepository.save(adEntity);
    }

    public boolean isAuthorAd(String username, Integer adId) {
        log.info("Использован метод сервиса: {}", LoggingMethodImpl.getMethodName());

        AdEntity adEntity = adRepository.findById(adId).orElseThrow(RuntimeException::new);
        return adEntity.getAuthor().getUserName().equals(username);
    }

}
