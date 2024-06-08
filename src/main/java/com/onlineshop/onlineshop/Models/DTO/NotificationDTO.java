package com.onlineshop.onlineshop.Models.DTO;

import com.onlineshop.onlineshop.Models.EverythingElse.Notification;

public class NotificationDTO {
    private int id;
    private String header;
    private String text;
    private boolean isRead;

    public NotificationDTO(){

    }

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.header = notification.getHeader();
        this.text = notification.getText();
        this.isRead = notification.isRead();
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public boolean isRead() {
        return isRead;
    }
}
