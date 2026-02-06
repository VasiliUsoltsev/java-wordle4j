package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class FileLogger {
    private boolean isConsole;
    private String fileName;
    public FileLogger() {
        this.isConsole = true;
        PrintWriter logFile = new PrintWriter(System.out, true);
        logFile.write("+".repeat(20) + "Старт новой игры!" + "+".repeat(20) + "\n");
        logFile.flush();
    }

    public FileLogger(String fileName) {
        this.isConsole = false;
        this.fileName = fileName;
        try (PrintWriter logFile = new PrintWriter(new FileWriter(fileName, true))) {
                logFile.write("+".repeat(20) + "Старт новой игры!" + "+".repeat(20) + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки модуля логирования!");
        }
    }

    public void addLog(String logText) {
        if (isConsole) {
            PrintWriter logFile = new PrintWriter(System.out, true);
            logFile.write("--> " + logText + "\n");
            logFile.flush();
        } else {
            try (PrintWriter logFile = new PrintWriter(new FileWriter(fileName, true))) {
                logFile.write("--> " + logText + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка загрузки модуля логирования!");
            }
        }
    }

}
