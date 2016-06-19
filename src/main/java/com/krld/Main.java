package com.krld;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.krld.formapps.ExampleFormApp;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        new ExampleFormApp().run();
    }
}
