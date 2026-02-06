package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.exceptions.game.WordIsWrongLength;
import ru.yandex.practicum.exceptions.game.WordNotFoundInDictionary;
import ru.yandex.practicum.exceptions.game.WordNotRussian;
import ru.yandex.practicum.exceptions.program.DictionaryEmpty;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    public static final String PATH_DICTIONARY = "test/resources/words_ru.txt";
    public static final String NAME_FILE_LOGS = "test/resources/logs/logs.txt";
    static FileLogger console;
    static WordleGame game;
    static WordleDictionaryLoader loader;

    @BeforeEach
    void inits() throws IOException, DictionaryEmpty {
        console = new FileLogger();
        loader = new WordleDictionaryLoader(console);
        game = new WordleGame(loader.loadDictionary(PATH_DICTIONARY), console);

    }
    @DisplayName("Проверка вариант пользовательского ввода слов, когда вводят латинские символы")
    @Test
    void checkInputWordRussian() throws WordIsWrongLength, WordNotFoundInDictionary {
        int result = -1;
        try {
             game.checkInputWord("reddd");
        } catch (WordNotRussian e) {
            result = 1;
        }

        Assertions.assertEquals(1, result);
    }

    @DisplayName("Проверка вариант пользовательского ввода слов, когда вводят короткие слова")
    @Test
    void checkInputWordShortLength() throws WordNotRussian, WordNotFoundInDictionary {
        int result = -1;
        try {
            game.checkInputWord("куе");
        } catch (WordIsWrongLength e) {
            result = 1;
        }
        Assertions.assertEquals(1, result);
    }

    @DisplayName("Проверка вариант пользовательского ввода слов, когда вводят длинные слова")
    @Test
    void checkInputWordLongLength() throws WordNotRussian, WordNotFoundInDictionary {
        int result = -1;
        try {
            game.checkInputWord("куекуауук");
        } catch (WordIsWrongLength e) {
            result = 1;
        }
        Assertions.assertEquals(1, result);
    }

    @DisplayName("Проверка вариант пользовательского ввода слов, когда вводят слова не из словаря")
    @Test
    void checkInputWordFromDictionary() throws WordIsWrongLength, WordNotRussian {
        int result = -1;
        try {
            game.checkInputWord("куеку");
        } catch (WordNotFoundInDictionary e) {
            result = 1;
        }
        Assertions.assertEquals(1, result);
    }

    @DisplayName("Проверка корректности подсказок")
    @Test
    void checkHint() {
        String secretWord = game.getSecretWord();
        for (int i = 0; i < secretWord.length(); i++)
            game.addRule(secretWord.charAt(i),'+', i);

        String temp = game.getHint();

        Assertions.assertEquals(secretWord, temp);
    }


    @DisplayName("Проверка алгоритма угадывания")
    @Test
    void checkAlgorithmSecret() {
        String secretWord = "сурок";
        String inputWord  = "сурна";
        game.setSecretWord(secretWord);
        String result = game.getFailedStep(inputWord);
        Assertions.assertEquals("+++--", result);
    }

    @DisplayName("Проверка надёжности функций валидации - угадал")
    @Test
    void checkValidationYes() {
        String inputWord  = game.getSecretWord();

        int result = 0;
        try {
            result = game.checkInputWord(inputWord);
        } catch (WordNotFoundInDictionary e) {
            console.addLog(e.getMessage());
        } catch (WordNotRussian e) {
            console.addLog(e.getMessage());
        } catch (WordIsWrongLength e) {
            console.addLog(e.getMessage());
        }

        Assertions.assertEquals(WordleGame.SECRET_WORD, result);
    }

    @DisplayName("Проверка надёжности функций валидации - не угадал")
    @Test
    void checkValidationNo() {
        String secretWord  = game.getSecretWord();
        String inputWord = secretWord.toUpperCase();

        int result = 0;
        try {
            result = game.checkInputWord(inputWord);
        } catch (WordNotFoundInDictionary e) {
            console.addLog(e.getMessage());
        } catch (WordNotRussian e) {
            console.addLog(e.getMessage());
        } catch (WordIsWrongLength e) {
            console.addLog(e.getMessage());
        }

        Assertions.assertEquals(WordleGame.NOT_SECRET_WORD, result);
    }

    @DisplayName("Проверка надёжности функций валидации - пустая строка")
    @Test
    void checkValidationEmpty() {
        String secretWord  = game.getSecretWord();
        String inputWord = "";

        int result = 0;
        try {
            result = game.checkInputWord(inputWord);
        } catch (WordNotFoundInDictionary e) {
            console.addLog(e.getMessage());
        } catch (WordNotRussian e) {
            console.addLog(e.getMessage());
        } catch (WordIsWrongLength e) {
            console.addLog(e.getMessage());
        }

        Assertions.assertEquals(WordleGame.EMPTY_INPUT, result);
    }

}
