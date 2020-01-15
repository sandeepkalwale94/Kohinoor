package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 28-01-2018.
 */

public class PushNotification {
   public String headingnoty;
   public String bodynoty;
   public String notesTypenoty;
   public String materialTypenoty;
   public String subItemnoty;
   public String key;
    PushNotification()
    {

    }

    public PushNotification(String heading, String body, String notesType, String materialType, String subItem, String key) {
        this.headingnoty = heading;
        this.bodynoty = body;
        this.notesTypenoty = notesType;
        this.materialTypenoty = materialType;
        this.subItemnoty = subItem;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getHeadingnoty() {
        return headingnoty;
    }

    public String getBodynoty() {
        return bodynoty;
    }

    public String getNotesTypenoty() {
        return notesTypenoty;
    }

    public String getMaterialTypenoty() {
        return materialTypenoty;
    }

    public String getSubItemnoty() {
        return subItemnoty;
    }
}
