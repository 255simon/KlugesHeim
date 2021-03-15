package com.example.klugesheim;

public class Switch {
    private String name;
    private String onCommand;
    private String offCommand;

    public Switch(String name, String command){
        this.name = name;
        this.onCommand = command + " --on";
        this.offCommand = command + " --off";
    }

    public String getName(){
        return this.name;
    }

    public String getOnCommand(){
        return this.onCommand;
    }

    public String getOffCommand(){
        return this.offCommand;
    }
}
