package ru.skypro.homework.service.impl;

import ru.skypro.homework.service.LoggingMethod;

public class LoggingMethodImpl implements LoggingMethod {
    public static String getMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getMethodName();
    }
}
