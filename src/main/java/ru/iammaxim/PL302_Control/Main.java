package ru.iammaxim.PL302_Control;

import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by maxim on 5/1/17 at 10:25 AM.
 */
public class Main {
    private static boolean enableColor = true;
    private ArrayList<Register> registers = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        Net.ip = "192.168.0.223";

        if (enableColor)
            AnsiConsole.systemInstall();
        new Main().run();
        if (enableColor)
            AnsiConsole.systemUninstall();
    }

    public void run() throws FileNotFoundException {
        init();

        //start input thread
        new Thread(() -> {
            Scanner in = new Scanner(System.in);
            while (in.hasNext()) {
                try {
                    String s = in.nextLine();
                    Net.write(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                update();
                out();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() throws FileNotFoundException {
        File f = new File("registers.txt");
        if (!f.exists())
            throw new IllegalStateException("File \"registers.txt\" not found");

        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.isEmpty() || s.startsWith("//"))
                continue;
            String[] arr = s.split(":");
            registers.add(new Register(arr[0], arr[1]));
        }
    }

    private void update() {
        //PL302 can't handle too many registers in one request, so send them one by one
        registers.forEach(r -> {
            try {
                r.value = Net.read(r.name).getString(r.name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void out() {
        System.out.println(ansi().eraseScreen());
        registers.forEach(r -> {
            if (enableColor)
                System.out.println(ansi().fg(BLUE).bold().a(r.human_readable_name + ": ")
                        .boldOff().fgDefault().a(r.value).reset());
            else
                System.out.println(r.human_readable_name + ": " + r.value);
        });
    }
}
