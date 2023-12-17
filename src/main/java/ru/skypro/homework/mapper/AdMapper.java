package ru.skypro.homework.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AdMapper {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;


    /**
     * Entity -> dto mapping
     *
     * @param entity AdEntity entity class
     * @return Ad dto class
     */
    public Ad mapToAdDto(AdEntity entity) {
        Ad dto = new Ad();
        dto.setAuthor(entity.getAuthor().getId());
        if (entity.getPhoto() != null) {
            dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        }
        dto.setPk(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    /**
     * Dto -> entity mapping without image.
     * Image will be saved separately because it needs a created ad with id.
     *
     * @param dto CreateAds dto class
     * @return AdEntity entity class
     */
    public AdEntity mapToAdEntity(CreateOrUpdateAd dto, String username) {
        UserEntity author = userRepository.findUserEntityByUserName(username);
        if (author == null) {
            throw new UserNotFoundException("User not found");
        }
        AdEntity entity = new AdEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setAuthor(author);
        return entity;
    }

    /**
     * AdEntity entity -> ExtendedAd dto mapping
     *
     * @param entity AdEntity entity class
     * @return ExtendedAd dto class
     */
    public ExtendedAd mapToExtendedAdDto(AdEntity entity) {
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getUserName());
        dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    /**
     * Метод конвертирует {@link MultipartFile} в сущность {@link PhotoEntity}
     * @param image
     * @return photo сущность {@link PhotoEntity}
     * @throws IOException
     */
    public PhotoEntity mapMultipartFileToPhoto(MultipartFile image) throws IOException {
        PhotoEntity photo = new PhotoEntity();
        photo.setData(image.getBytes());
        photo.setMediaType(image.getContentType());
        photo.setFileSize(image.getSize());
        return photo;
    }
}
