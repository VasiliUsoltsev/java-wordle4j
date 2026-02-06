package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.program.DictionaryEmpty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final FileLogger logger;
    public WordleDictionaryLoader(FileLogger logger) {
        this.logger = logger;
    }
    public  WordleDictionary loadDictionary(String fileName) throws IOException, DictionaryEmpty {
        List<String> words = new ArrayList<>();
        String str;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while ((str = fileReader.readLine()) != null) {
                if (WordleDictionary.isWordLengthRequirements(str)) {
                    words.add(WordleDictionary.normalize(str));
                }
            }
        }
        if (words.isEmpty()) {
            throw new DictionaryEmpty("Словарь пуст!");
        }
        logger.addLog("Словарь загружен!");
        return new WordleDictionary(words, logger);
    }
}
