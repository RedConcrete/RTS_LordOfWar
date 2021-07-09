package code.lordofwar.backend;

import java.util.ArrayList;

import static code.lordofwar.backend.Constants.STRINGSEPERATOR;

/**
 *
 * @author Robin Hefner
 */
public class DataPacker {

    public static String stringCombiner(ArrayList<String> arrayList){
        StringBuilder newString = new StringBuilder();

        for (String s : arrayList) {
            newString.append(s);
            newString.append(STRINGSEPERATOR);
        }

        return newString.toString();
    }

    /**
     * prepares the data with an specified MessageID
     * @param messageIdentifier
     * @param data
     * @return
     */
    public static String packData(MessageIdentifier messageIdentifier, String data){
        String dataPackage = "";
        dataPackage = messageIdentifier + STRINGSEPERATOR + data;
        return dataPackage;
    }



}
