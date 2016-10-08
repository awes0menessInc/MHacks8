package org.octocats.lecturenotegenerator;

import java.util.Date;

/**
 * Created by nisarg on 8/10/16.
 */

public class Lecture {
    private String title;
    private Date date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Lecture(String title, Date date) {

        this.title = title;
        this.date = date;
    }
}
