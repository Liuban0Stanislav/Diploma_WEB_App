package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;

public interface PhotoService {
    byte[] getPhoto(Integer pk) throws IOException;
}
