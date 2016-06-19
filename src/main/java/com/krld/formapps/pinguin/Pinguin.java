package com.krld.formapps.pinguin;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Pinguin {

    private BasicWindow mainWindow;
    private ScheduledExecutorService stageQueue;
    private WindowBasedTextGUI gui;
    private volatile boolean isRunning;
    private TextBox textHostname;
    private Button buttonStart;
    private Button buttonEnd;
    private Table<String> table;
    private String sessionUUID;

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
        panel.setPreferredSize(new TerminalSize(72, 18));

        buttonStart = new Button("Start", this::start);
        buttonStart.setSize(new TerminalSize(8, 1));
        buttonStart.setPosition(new TerminalPosition(2, 1));
        panel.addComponent(buttonStart);

        buttonEnd = new Button("Stop", this::stop);
        buttonEnd.setSize(new TerminalSize(8, 1));
        buttonEnd.setPosition(new TerminalPosition(2 + 8 + 2, 1));
        panel.addComponent(buttonEnd);

        textHostname = new TextBox("192.168.0.6");
        textHostname.setSize(new TerminalSize(15, 1));
        //textHostname.setPosition(new TerminalPosition(2, buttonStart.getPosition().getRow() + 3));
        Border hostnameBorder = textHostname.withBorder(Borders.singleLine("Hostname"));
        hostnameBorder.setSize(new TerminalSize(15, 3));
        hostnameBorder.setPosition(new TerminalPosition(2, buttonStart.getPosition().getRow() + 2));
        panel.addComponent(hostnameBorder);

        table = new Table<>("Hostname", "Time Start", "Duration", "Period", "Result");
        Border tableWithBorder = table.withBorder(Borders.singleLine("Requests"));
        tableWithBorder.setSize(new TerminalSize(panel.getPreferredSize().getColumns() - 5, panel.getPreferredSize().getRows() - 8));
        tableWithBorder.setPosition(new TerminalPosition(hostnameBorder.getPosition().getColumn(), hostnameBorder.getPosition().getRow() + 4));
        table.setSize(new TerminalSize(tableWithBorder.getSize().getColumns() - 2, tableWithBorder.getSize().getRows() - 2));
        table.setVisibleRows(table.getSize().getRows() - 1);
        panel.addComponent(tableWithBorder);

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

    private void addRow() {
        ArrayList<String> row = new ArrayList<>();
        row.add("d");
        row.add("d");
        row.add("d");
        row.add("d");
        row.add("d");
        table.getTableModel().insertRow(0, row);
    }

    private void stop() {
        stageQueue.submit(() -> {
            if (isRunning) {
                setIsRunning(false);
                sessionUUID = "no uuid";
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
                sessionUUID = UUID.randomUUID().toString();
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
                                    stageQueue.submit(() -> {
                                        //TODO
                                    });
                                },
                                (call, e) -> {
                                    stageQueue.submit(() -> {
                                        addRow();
                                        System.out.println("error: " + e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
                                    });
                                },
                                textHostname.getText(),
                                sessionUUID);
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
     /*       buttonStart.setEnabled(false); //TODO style for buttons
            buttonEnd.setEnabled(true);*/

            mainWindow.setTitle("Pinguin: RUNNING");
        } else {
       /*     buttonStart.setEnabled(true);
            buttonEnd.setEnabled(false);*/

            mainWindow.setTitle("Pinguin: STOPPED");
        }

    }
}
