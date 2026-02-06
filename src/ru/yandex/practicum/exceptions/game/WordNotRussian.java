package ru.yandex.practicum.exceptions.game;

public class WordNotRussian extends Exception {
    public WordNotRussian(final String message) {
        super(message);
    }
}
