package ru.yandex.practicum;

import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    public static final int LENGTH_WORD = 5;

    private final List<String> words;
    private final FileLogger logger;

    public WordleDictionary(List<String> words,FileLogger logger) {
        this.words = words;
        this.logger = logger;
    }

    // Проверка на длину слова
    public static boolean isWordLengthRequirements(String word) {
        return word.length() == LENGTH_WORD;
    }

    // Нормализуем(приводим к нужному виду) слово перед добавлением в словарь
    public static String normalize(String word) {
        return word.replace("ё", "e").toLowerCase();
    }

    // Получить значение словаря по индексу листа
    public String getWord(int index) {
        return words.get(index);
    }

    // Получить все слова из словаря
    public int size() {
        return words.size();
    }

    public boolean searchWord(String inputWord) {
        return words.contains(inputWord);
    }

    public List<String> getDictionary() {
        return words;
    }



}
