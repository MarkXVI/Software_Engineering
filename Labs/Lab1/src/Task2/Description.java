package Task2;

import java.io.Serializable;
import java.util.Date;

public class Description implements Serializable {
    private String text;
    private Date addedDate;

    Description(String text, Date date){
        this.text = text;
        this.addedDate = date;
    }


    public Date getAddedDate() {
        return addedDate;
    }


    public String getText() {
        return text;
    }

}
