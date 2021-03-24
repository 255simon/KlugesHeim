package com.example.klugesheim;

public class Switch {
    private String name = null;
    private String command = null;
    private String onCommand = null;
    private String offCommand = null;

    public Switch(String name, String command){
        this.name = name;
        this.onCommand = command + " --on";
        this.offCommand = command + " --off";
        this.command = command;
    }

    public Switch(){};

    public String getName(){
        return this.name;
    }

    public String getCommand(){ return this.command; }

    public String getOnCommand(){
        return this.onCommand;
    }

    public String getOffCommand(){ return this.offCommand; }

    public void setName(String name){
        this.name = name;
    }

    public void setCommand(String command){
        this.command = command;
        this.onCommand = command + " --on";
        this.offCommand = command + " --off";
    }
}
