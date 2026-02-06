package ru.yandex.practicum.exceptions.game;

public class WordIsWrongLength extends Exception {
    public WordIsWrongLength(final String message) {
        super(message);
    }
}
