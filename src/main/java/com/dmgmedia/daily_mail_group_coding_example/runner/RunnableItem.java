package com.dmgmedia.daily_mail_group_coding_example.runner;

public interface RunnableItem {

    void run() throws BrowserException;

    String getDescription();

    boolean getStatus();

    void setStatus(boolean status);

    String getResult();

    void setResult(String result);
}
