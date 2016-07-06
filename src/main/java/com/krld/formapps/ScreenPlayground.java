package com.krld.formapps;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class ScreenPlayground {
    private static Terminal terminal;
    private static Screen screen;
    private static int x;
    private static int y;

    public static void main(String[] args) throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();

        x = 10;
        y = 12;
        while (true) {
            drawPlayer(x, y);
            screen.refresh();
            KeyStroke keyStroke = screen.readInput();
            if (keyStroke != null) {
                drawSpace(x, y);
                String input = keyStroke.getCharacter().toString();
                if (input.equalsIgnoreCase("s")) {
                    y++;
                } else if (input.equalsIgnoreCase("w")) {
                    y--;
                } else if (input.equalsIgnoreCase("d")) {
                    x++;
                } else if (input.equalsIgnoreCase("a")) {
                    x--;
                }
            }
        }
    }

    private static void drawPlayer(int row, int column) {
        screen.setCharacter(row, column, new TextCharacter('@', TextColor.ANSI.MAGENTA, TextColor.ANSI.WHITE));
    }

    private static void drawSpace(int row, int column) {
        screen.setCharacter(row, column, new TextCharacter('.', new TextColor.RGB(10, 10, 10), new TextColor.RGB(44, 44, 44)));
    }
}
