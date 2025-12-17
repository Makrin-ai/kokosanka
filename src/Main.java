/*
Разработать эмулятор для языка оболочки ОС. Необходимо сделать работу
эмулятора как можно более похожей на работу в командной строке UNIX-
подобной ОС.
 */

import java.io.*;
import java.net.InetAddress;
import java.util.*;

public class Main {

    private static String vfsPath = null;
    private static String scriptPath = null;
    private static boolean running = true;

    //Приложение должно быть реализовано в форме консольного интерфейса
    //(CLI)
    public static void main(String[] args) {
        parseArgs(args);
        printDebugInfo();

        if (scriptPath != null) {
            runScript(scriptPath);
        }

        runREPL();
    }

    /* ===================== ЭТАП 2: ПАРАМЕТРЫ ===================== */
//сделать эмулятор настраиваемым, то есть поддержать ввод параметров
//пользователя в приложение. Организовать для этого этапа отладочный вывод всех
//заданных параметров при запуске эмулятора.
//Требования:
//1. Параметры командной строки:
//– Путь к физическому расположению VFS.
//– Путь к стартовому скрипту.
    private static void parseArgs(String[] args) {
        //Параметры разбираются при запуске и сохраняются в переменные
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--vfs":
                    if (i + 1 < args.length) {
                        vfsPath = args[++i];
                    }
                    break;
                case "--script":
                    if (i + 1 < args.length) {
                        scriptPath = args[++i];
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
            }
        }
    }

    //Стартовый скрипт для выполнения команд эмулятора: выполняет команды
    //последовательно, ошибочные строки пропускает. При выполнении скрипта
    //на экране отображается как ввод, так и вывод, имитируя диалог с
    //пользователем.
    private static void printDebugInfo() {
        System.out.println("=== Emulator configuration ===");
        System.out.println("VFS path: " + (vfsPath != null ? vfsPath : "not set"));
        System.out.println("Startup script: " + (scriptPath != null ? scriptPath : "not set"));
        System.out.println("================================");
    }

    /* ===================== REPL ===================== */

    //Приложение должно быть реализовано в форме консольного интерфейса
    //(CLI)
    //Ввод команд осуществляется через Scanner(System.in),
    // вывод — через System.out и System.err.
    //Программа полностью работает в терминале, графический интерфейс отсутствует → CLI.
    private static void runREPL() {
        Scanner scanner = new Scanner(System.in);

        //Флаг running завершает цикл REPL → программа корректно выходит
        while (running) {
            System.out.print(buildPrompt());
            String line;

            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                break;
            }

            executeLine(line, true);
        }
    }

    //Приглашение к вводу должно формироваться на основе реальных данных
    //ОС, в которой исполняется эмулятор. Пример: username@hostname:~$.
    private static String buildPrompt() {
        String user = System.getProperty("user.name");
        String host = "unknown";

        try {
            host = InetAddress.getLocalHost().getHostName(); //имя хоста
        } catch (Exception ignored) {}

        return user + "@" + host + ":~$ ";
    }

    /* ===================== СКРИПТ ===================== */

    private static void runScript(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Script not found: " + path);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            //ошибки не прерывают выполнение
            while ((line = reader.readLine()) != null) { //выполняется построчно
                System.out.print(buildPrompt());
                System.out.println(line);
                executeLine(line, false);
            }
            //Сообщить об ошибке во время исполнения стартового скрипта.
        } catch (IOException e) {
            System.err.println("Error executing script: " + e.getMessage());
        }
    }

    /* ===================== ПАРСИНГ И ВЫПОЛНЕНИЕ ===================== */

    private static void executeLine(String line, boolean interactive) {
        line = expandEnvVars(line).trim();

        if (line.isEmpty()) return;

        String[] tokens = line.split("\\s+");
        String command = tokens[0];
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        //Реализовать команды-заглушки, которые выводят свое имя и аргументы: ls,
        //cd
        switch (command) {
            case "ls":
                cmdLs(args);
                break;
            case "cd":
                cmdCd(args);
                break;
                //Реализовать команду exit
            case "exit":
                running = false;
                break;
                //Продемонстрировать работу прототипа в интерактивном режиме.
            //Необходимо показать примеры работы всей реализованной
            //функциональности, включая обработку ошибок.
            default:
                System.err.println("Command not found: " + command); //Неизвестные команды не приводят к падению программы,
                // а выводят сообщение об ошибке.
        }
    }

    /* ===================== РАСКРЫТИЕ $VARS ===================== */

    //Реализовать парсер, который поддерживает раскрытие переменных
    //окружения реальной ОС (например, $HOME)
    //Метод заменяет все вхождения $VAR на значения из System.getenv()
    private static String expandEnvVars(String line) {
        for (Map.Entry<String, String> env : System.getenv().entrySet()) {
            line = line.replace("$" + env.getKey(), env.getValue());
        }
        return line;
    }

    /* ===================== КОМАНДЫ ===================== */

    //Реализация заглушек:
    private static void cmdLs(String[] args) {
        System.out.println("ls called with arguments:");
        for (String arg : args) {
            System.out.println("  " + arg);
        }
    }

    private static void cmdCd(String[] args) {
        System.out.println("cd called with arguments:");
        for (String arg : args) {
            System.out.println("  " + arg);
        }
    }
}
