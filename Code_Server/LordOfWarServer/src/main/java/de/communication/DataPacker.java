package de.communication;

import de.constants.MessageIdentifier;

import java.util.ArrayList;

import static de.constants.Constants.STRINGSEPERATOR;

public class DataPacker {

    public static String stringCombiner(ArrayList<String> arrayList){
        StringBuilder newString = new StringBuilder();

        for (String s : arrayList) {
            newString.append(s);
            newString.append(STRINGSEPERATOR);
        }
        return newString.toString();
    }

    public static String packData(MessageIdentifier messageIdentifier, String data){
        String dataPackage = "";
        dataPackage = messageIdentifier + STRINGSEPERATOR + data;
        return dataPackage;
    }



}
