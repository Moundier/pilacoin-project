package br.ufsm.csi.tapw.pilacoin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class JournalUtil {

    private static Logger getInstance() {
        
        String string = Thread
            .currentThread()
            .getStackTrace()[3]
            .getClassName();

        return LoggerFactory.getLogger(string);
    }

    public static void log(String message) {
        getInstance().info(message);
    }

    // Difficulty Service and Queue Service
    public static void logRoundBox(String message) {
        List<String> lines = List.of(message.split("\n"));
        int boxWidth = lines.stream().max(Comparator.comparingInt(String::length)).get().length();

        getInstance().info("╭" + "─".repeat(boxWidth + 2) + "╮");
        
        for (String line : lines) {
            if (line.startsWith("---") || line.endsWith("---")) {
                getInstance().info("├" + "─".repeat(boxWidth + 2) + "┤");

                continue;
            }

            getInstance().info("│ " + String.format("%-" + boxWidth + "s", line) + " │");
        }

        getInstance().info("╰" + "─".repeat(boxWidth + 2) + "╯");
    }

    public static void logDoubleLineBox(String message) {
        List<String> lines = List.of(message.split("\n"));
        int boxWidth = lines.stream().max(Comparator.comparingInt(String::length)).get().length();
    
        getInstance().info("╔" + "═".repeat(boxWidth + 2) + "╗");
    
        for (String line : lines) {
            if (line.startsWith("---") || line.endsWith("---")) {
                getInstance().info("╠" + "═".repeat(boxWidth + 2) + "╣");
                continue;
            }
    
            getInstance().info("║ " + String.format("%-" + boxWidth + "s", line) + " ║");
        }
    
        getInstance().info("╚" + "═".repeat(boxWidth + 2) + "╝");
    }
}