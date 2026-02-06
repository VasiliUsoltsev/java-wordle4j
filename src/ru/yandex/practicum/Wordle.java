package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.game.WordIsWrongLength;
import ru.yandex.practicum.exceptions.game.WordNotFoundInDictionary;
import ru.yandex.practicum.exceptions.game.WordNotRussian;
import ru.yandex.practicum.exceptions.program.DictionaryEmpty;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    public static final String PATH_DICTIONARY = "src/resources/words_ru.txt";
    public static final String NAME_FILE_LOGS = "src/resources/logs/logs.txt";
    private static WordleGame game;

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в игру \"Wordle\"!");
        FileLogger logger = new FileLogger(NAME_FILE_LOGS);
        try {
            Scanner scanner = new Scanner(System.in);
            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            game = new WordleGame(loader.loadDictionary(PATH_DICTIONARY), logger);
            System.out.println("Загаданное слово: " + game.getSecretWord());
            while (game.nextStep()) {
                try {
                    System.out.println("Введи слово(вариант " + game.getStep() + "):");
                    System.out.print("-> ");
                    gameRound(scanner.nextLine());
                } catch (WordNotFoundInDictionary | WordNotRussian | WordIsWrongLength e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Игра завершена!");
        } catch (DictionaryEmpty | IOException e) {
            String er = "Ошибка загрузки - игра не смогла подгрузить словарь!";
            System.out.println(er);
            logger.addLog(er + "\n" + e.getMessage());
        }

    }

    public static void gameRound(String inputWord) throws WordNotFoundInDictionary, WordNotRussian, WordIsWrongLength {
        int result = game.checkInputWord(inputWord);
        switch (result) {
            case WordleGame.SECRET_WORD:
                // Выйгрыш
                System.out.println("Вы угадали!");
                return;
            case WordleGame.NOT_SECRET_WORD:
                //Проигрыш
                System.out.println("-> " + game.getFailedStep(inputWord));
                System.out.println("Неудачная попытка!");
                break;
            case WordleGame.EMPTY_INPUT:
                // Подсказка
                String hint = game.getHint();
                System.out.println("Строка подсказка - " + hint);
                gameRound(hint);
                break;
        }
    }
}
