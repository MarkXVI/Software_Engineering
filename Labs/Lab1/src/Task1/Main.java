package Task1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) {
        String filename = "./src/Task1/HBG.ser";
        if(args.length > 0){
            filename = args[0];
        }
        LatLng latLng = new LatLng(40.2732, 76.8867);
        FileOutputStream fileOut;
        ObjectOutputStream objOut;
        try {
            
            fileOut = new FileOutputStream(filename);
            
            objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(latLng);
            objOut.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
