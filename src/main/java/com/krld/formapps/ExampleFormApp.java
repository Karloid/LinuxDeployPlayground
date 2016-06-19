package com.krld.formapps;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ExampleFormApp {

    private BasicWindow window;
    private Terminal terminal;

    public void run() {
        try {
            runSumForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runSumForm() throws IOException {
        System.out.println("*** starting app ***");
        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.setPreferredSize(new TerminalSize(40, 10));

        final Label lblOutput = new Label("");

        panel.addComponent(new Label("Num 1"));
        final TextBox txtNum1 = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(panel);

        panel.addComponent(new Label("Num 2"));
        final TextBox txtNum2 = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(panel);

        panel.addComponent(new Label("Operation"));
        final ComboBox<String> operations = new ComboBox<String>();
        operations.addItem("Add");
        operations.addItem("Subtract");
        panel.addComponent(operations);

        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        new Button("Calculate!", () -> {
            if (txtNum1.getText().length() == 0 || txtNum2.getText().length() == 0) {
                return;
            }

            int num1 = Integer.parseInt(txtNum1.getText());
            int num2 = Integer.parseInt(txtNum2.getText());
            if (operations.getSelectedIndex() == 0) {
                lblOutput.setText(Integer.toString(num1 + num2));
            } else if (operations.getSelectedIndex() == 1) {
                lblOutput.setText(Integer.toString(num1 - num2));
            }
        }).addTo(panel);

        Button buttonTest = new Button("Test", () -> {
            window.close();
            window = null;
        });

        panel.addComponent(buttonTest);
        panel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        panel.addComponent(lblOutput);

        // Create window to hold the panel
        window = new BasicWindow();
        window.setComponent(panel);
        window.setTitle("Calculator");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        MultiWindowTextGUI gui = new MultiWindowTextGUI(createScreen(), new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.CYAN));
        gui.addWindowAndWait(window);
    }

    private Screen createScreen() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        return screen;
    }
}
