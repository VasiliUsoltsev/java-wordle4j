package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.game.WordIsWrongLength;
import ru.yandex.practicum.exceptions.game.WordNotFoundInDictionary;
import ru.yandex.practicum.exceptions.game.WordNotRussian;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private static final int MAX_STEPS_GAME = 6;
    public static final int NOT_SECRET_WORD = 0;
    public static final int SECRET_WORD = 1;
    public static final int EMPTY_INPUT = 2;
    public static final int ERROR_CHECK_WORD = -1;

    // Загаданное слово
    private String secretWord;

    // Шаги(циклы)
    private int steps;

    // Словарь
    private final WordleDictionary dictionary;

    // Список выявленных правил
    private final List<RuleSecretWord> rules;

    private final FileLogger logger;

    public WordleGame(WordleDictionary dictionary, FileLogger logger) {
        this.dictionary = dictionary;
        this.steps = 0;
        this.secretWord = dictionary.getWord(new Random().nextInt(dictionary.size()));
        logger.addLog("Сгенерировано загаданное слово - " + secretWord);
        rules = new ArrayList<>();
        this.logger = logger;
    }

    public String getSecretWord() {
        return secretWord;
    }

    // Новый цикл игры
    public boolean nextStep() {
        logger.addLog("Начинается новый цикл игры - " + (steps + 1));
        return steps++ <= MAX_STEPS_GAME - 1;
    }

    public int getStep() {
        return steps;
    }

    // Проверка введенного слова с загаданным
    public int checkInputWord(String inputWord) throws WordNotFoundInDictionary, WordNotRussian, WordIsWrongLength {
        logger.addLog("Проверяем введенное слово - \'" + inputWord + "\'");
        if (inputWord.isBlank()) {
            logger.addLog("Пользователь не ввел слово, будет сгенерирована подсказка");
            return EMPTY_INPUT;
        }

        if (!isRussianWord(inputWord)) {
            throw new WordNotRussian("Введенное содержит латинские символы!");
        }

        if (inputWord.length() != WordleDictionary.LENGTH_WORD) {
            throw new WordIsWrongLength("Введенное слово неправильной длинны!");
        }

        if (!dictionary.searchWord(inputWord)) {
            throw new WordNotFoundInDictionary("Введенное слово отсутствует в словаре!");
        }

        if (inputWord.equals(secretWord)) {
            // Слово угадано
            // Устанавливаем конечный шаг
            logger.addLog("Слово угадано на " + steps + " шаге, секретное слово - \'" + secretWord + "\'");
            steps = MAX_STEPS_GAME;
            return SECRET_WORD;
        } else {
            // Слово не угадано
            logger.addLog("Слово не угадано - \'" + inputWord + "\' != \'" + secretWord + "\'");
            return NOT_SECRET_WORD;
        }
    }

    private boolean isRussianWord(String inputWord) {
        // Кириллические буквы (включая Ё и ё)
        for (int i = 0; i < inputWord.length(); i++) {
            if (!((inputWord.charAt(i) >= 'А' && inputWord.charAt(i) <= 'Я') ||
                    (inputWord.charAt(i) >= 'а' && inputWord.charAt(i) <= 'я') ||
                    inputWord.charAt(i) == 'Ё' || inputWord.charAt(i) == 'ё')) {
                return false;
            }
        }
        return true;
    }

    // Возвращаем результат в случае неправильно введенного слова и фиксируем правила для фильтрации
    public String getFailedStep(String inputWord) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < WordleDictionary.LENGTH_WORD; i++) {
            if (inputWord.charAt(i) == secretWord.charAt(i)) {
                // Совпадение по буквам
                logger.addLog("Есть совпадение по символу - \'" + inputWord.charAt(i) + "\', на позиции - " + i);
                result.append("+");
                addRule(inputWord.charAt(i), '+', i);
            } else if (secretWord.contains(String.valueOf(inputWord.charAt(i)))) {
                // Буква не на месте, но есть
                logger.addLog("Есть совпадение по символу - \'" + inputWord.charAt(i) +
                        "\', но не на своем месте");
                result.append("^");
                addRule(inputWord.charAt(i), '^', -1);
            } else {
                // Не совпадения по буквам
                logger.addLog("Символа - \'" + inputWord.charAt(i) + "\' нет в загаданном слове");
                result.append("-");
                addRule(inputWord.charAt(i), '-', i);
            }
        }
        logger.addLog("На данный момент зафиксированы следующие правила: \n" + rules);
        return result.toString();
    }

    // Проверка на дубли правила для загаданного слова
    private boolean checkRule(RuleSecretWord rule) {
        return !rules.contains(rule);
    }

    // Добавление правила в список
    public void addRule(char symbol, char sign, int sequenceNumber) {
        RuleSecretWord rule = null;
        switch (sign) {
            case '^':
                rule = new RuleSecretWord(symbol, sign);
                break;
            default:
                rule = new RuleSecretWord(symbol, sign, sequenceNumber);
        }
        if (checkRule(rule)) {
            rules.add(rule);
            logger.addLog("Правило \'" + sign + "\' на символ - \'" + symbol + "\' создано");
        } else logger.addLog("Данное правило уже существует - " + rule);
    }

    // Генерация слова подсказки для пользователя
    public String getHint() {
        // Перед генерацией подсказки необходимо почистить словарь от слов, которые в данный момент не подходят
        filterDictionary();
        // Генерация слова
        return dictionary.getWord(new Random().nextInt(dictionary.size()));
    }

    // Проверка словаря на выявленные правила
    private void filterDictionary() {
        logger.addLog("Список слов до чистки - " + dictionary.getDictionary());
        Iterator<String> iterator = dictionary.getDictionary().iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (!isRuleWord(item)) iterator.remove();
        }
        logger.addLog("Список слов после чистки - " + dictionary.getDictionary());
        // Очищаем список правил, так как по нему прошлись уже
        rules.clear();
    }


    // Проверяем слово по зафиксированным правилам
    private boolean isRuleWord(String word) {
        boolean flag = true;
        for (RuleSecretWord rule : rules)
            switch (rule.getSign()) {
                case '+':
                    if (word.charAt(rule.getSequenceNumber()) != rule.getSymbol()) flag = false;
                    break;
                case '-':
                    if (word.charAt(rule.getSequenceNumber()) == rule.getSymbol()) flag = false;
                    break;
                case '^':
                    if (!word.contains(String.valueOf(rule.getSymbol()))) flag = false;
                    break;
            }
        return flag;
    }

    public List<String> getDictionary() {
        return dictionary.getDictionary();
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

}
