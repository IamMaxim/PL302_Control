package ru.iammaxim.PL302_Control;

/**
 * Created by maxim on 5/1/17 at 11:19 AM.
 */
public class Register {
    public String name, value, human_readable_name;

    public Register(String name, String human_readable_name) {
        this.name = name;
        this.human_readable_name = human_readable_name;
    }
}
