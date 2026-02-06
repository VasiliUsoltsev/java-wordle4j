package ru.yandex.practicum.exceptions.game;


public class WordNotFoundInDictionary extends Exception{
    public WordNotFoundInDictionary(final String message) {
        super(message);
    }
}
