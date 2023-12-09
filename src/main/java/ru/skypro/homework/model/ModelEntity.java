package ru.skypro.homework.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.impl.LoggingMethodImpl;

@Data
public class ModelEntity {
    private PhotoEntity photo;
    private String filePath; //путь на ПК
    private String image; //URL для контроллера
}
