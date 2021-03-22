package code.lordofwar.backend;

import java.util.ArrayList;

import static code.lordofwar.backend.Constants.STRINGSEPERATOR;

public class DataPacker {

    public DataPacker() {

    }

    public String stringCombiner(ArrayList<String> arrayList){
        StringBuilder newString = new StringBuilder();

        for (String s : arrayList) {
            newString.append(s);
            newString.append(STRINGSEPERATOR);
        }

        return newString.toString();
    }

    public String packData(MessageIdentifier messageIdentifier, String data){
        String dataPackage = "";
        dataPackage = messageIdentifier + STRINGSEPERATOR + data;
        return dataPackage;
    }



}
