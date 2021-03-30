package de.main;

import java.util.ArrayList;

import static de.main.Constants.STRINGSEPERATOR;

/**
 * //todo kurz erklären!
 *
 * @author Franz Klose
 */
public class DataPacker {

    /**
     * //todo kurz erklären!
     *
     * @author Franz Klose
     */
    public String stringCombiner(ArrayList<String> arrayList){
        StringBuilder newString = new StringBuilder();

        for (String s : arrayList) {
            newString.append(s);
            newString.append(STRINGSEPERATOR);
        }
        return newString.toString();
    }

    /**
     * //todo kurz erklären!
     *
     * @author Franz Klose
     */
    public String packData(MessageIdentifier messageIdentifier, String data){
        String dataPackage = "";
        dataPackage = messageIdentifier + STRINGSEPERATOR + data;
        return dataPackage;
    }



}
