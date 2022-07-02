package com.kral1k.krabot;

import java.util.Scanner;

public class Main {
    private static final Bot BOT = new Bot();

    public static void main(String[] args1) throws Throwable {
        BOT.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String[] args = scanner.nextLine().split("\s");
            switch (args[0].toLowerCase()) {
                case "commands" -> commandUpdate(args);
                case "stop" -> stopCommand();
                default -> System.out.println("Неизвестная команда!");
            }
        }
    }

    public static void stopCommand() {
        BOT.stop();
        System.exit(1);
    }

    public static void commandUpdate(String[] args) {
        if (args.length < 1) {
            System.out.println("Неизвестная команда!");
        }
        if (args[1].equalsIgnoreCase("update")) {
            int i = BOT.updateCommands();
            System.out.println("обновлено " + i + " команд");
        }
    }
}
