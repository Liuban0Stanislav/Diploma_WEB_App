package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/ads")
public class AdController {
    private final AdService adService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("За запущен метод контроллера: getAllAds");
        return ResponseEntity.ok(adService.getAllAds());
    }
    @Operation(
        summary = "Добавление объявления",
        tags = {"Объявления"})
@ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                description = "Created",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Ad.class))),
        @ApiResponse(responseCode = "401",
                description = "Unauthorized"),
        @ApiResponse(responseCode = "403",
                description = "Forbidden"),
        @ApiResponse(responseCode = "404",
                description = "Not Found")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> addAd(@RequestPart(value = "properties", required = false) CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) throws IOException {
        log.info("За запущен метод контроллера: addAd");
        return ResponseEntity.ok(adService.addAd(properties, image, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        log.info("За запущен метод контроллера: getAds");
        ExtendedAd ad = adService.getAds(id);
        if (ad != null) {
            return ResponseEntity.ok(ad);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.name, #adId)")
    public ResponseEntity removeAd(@PathVariable("id") Integer adId) {
        log.info("За запущен метод контроллера: removeAd");
        return (adService.removeAd(adId)) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.name, #adId)")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Integer adId, @RequestBody CreateOrUpdateAd dto) {
        log.info("За запущен метод контроллера: updateAds");
        Ad ad = adService.updateAds(adId, dto);
        if (ad != null) {
            return ResponseEntity.ok(ad);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("За запущен метод контроллера: getAdsMe");
        if (authentication.getName() != null) {   //если пользователь авторизовался
            String username = authentication.getName();
            return ResponseEntity.ok(adService.getAdsMe(username));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Переданный файл слишком большой",
                            content = @Content(
                            )
                    )
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.name, #adId)")
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer adId,
                                              @RequestParam MultipartFile image,
                                              Authentication authentication) throws IOException {
        log.info("За запущен метод контроллера: updateImage");
        /*if (image.getSize() > 1024 * 1024 * 2) {
            return ResponseEntity.status(HttpStatus.valueOf(404)).build();
        }
        if (authService.getLogin() != null) {
            PhotoEntity photo = adService.updateImage(id, image);
            return ResponseEntity.ok(photo); // todo продумать, что метод контроллера возвращает, как вариант PhotoEntity
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //return null; //todo разобраться с ошибками 403 и 404, как и в остальных метод контроллераах выше, если есть*/

        if ( authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // todo посмотреть как это сделать с Security
        }

        adService.updateImage(adId, image);

        PhotoEntity photo = adService.findPhoto(adId);
        if (photo != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(photo.getMediaType()));
            headers.setContentLength(photo.getData().length);

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(photo.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}