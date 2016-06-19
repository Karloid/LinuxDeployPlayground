package com.krld.formapps;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private BasicWindow mainWindow;
    private ScheduledExecutorService stageQueue;
    private WindowBasedTextGUI gui;
    private volatile boolean isRunning;

    public void run() {

        stageQueue = Executors.newScheduledThreadPool(1);
        try {
            createGui();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stageQueue.shutdown();
        System.out.println("end of main thread");
    }

    private void createGui() throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        gui = new MultiWindowTextGUI(screen);

        Panel panel = new Panel(new AbsoluteLayout());
        panel.setPreferredSize(new TerminalSize(72, 10));

        Button buttonStart = new Button("Start", this::start);
        buttonStart.setSize(new TerminalSize(8, 1));
        buttonStart.setPosition(new TerminalPosition(2, 1));
        panel.addComponent(buttonStart);

        Button buttonEnd = new Button("Stop", this::stop);
        buttonEnd.setSize(new TerminalSize(8, 1));
        buttonEnd.setPosition(new TerminalPosition(2 + 8 + 2, 1));
        panel.addComponent(buttonEnd);

        Button buttonAppClose = new Button("Close", this::finishAndClose);
        buttonAppClose.setSize(new TerminalSize(8, 1));
        buttonAppClose.setPosition(new TerminalPosition(panel.getPreferredSize().getColumns() - 10, 1));
        panel.addComponent(buttonAppClose);
        mainWindow = new BasicWindow();
        mainWindow.setComponent(panel);
        gui.addWindow(mainWindow);
        mainWindow.setPosition(new TerminalPosition(2, 1));

        setIsRunning(false);
        gui.waitForWindowToClose(mainWindow);
    }

    private void stop() {
        stageQueue.submit(() -> {
            if (isRunning) {
                setIsRunning(false);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Already stopped!");
            }
        });
    }

    private void start() {
        stageQueue.submit(() -> {
            if (isRunning) {
                MessageDialog.showMessageDialog(gui, "Error", "Already running!");
            } else {
                setIsRunning(true);

                doPingServer();
            }
        });
    }

    private void doPingServer() {
        stageQueue.submit(() -> {
            if (!isRunning) {
                return;
            }
            try {


                System.out.println("do ping server");
                WebClient.getInstance()
                        .sendPing(
                                (call, response) -> {
                                    System.out.println("success");
                                },
                                (call, e) -> {
                                    System.out.println("error: " + e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
                                }, "192.168.0.6");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stageQueue.schedule(this::doPingServer, 1000, TimeUnit.MILLISECONDS);
        });
    }

    private void finishAndClose() {
        mainWindow.close();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
        if (isRunning) {
            mainWindow.setTitle("Pinguin: RUNNING");
        } else {
            mainWindow.setTitle("Pinguin: STOPPED");
        }

    }
}
