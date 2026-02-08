package ru.yandex.practicum.exceptions.program;

public class DictionaryEmpty extends RuntimeException {
    public DictionaryEmpty(final String message) {
        super(message);
    }
}
