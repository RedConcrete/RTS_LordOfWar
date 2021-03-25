package code.lordofwar.backend;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class CommunicationHandler {


    private WebSocketListener gameWebSocketListener = null;

    public CommunicationHandler() {


    }

    public void registerMessage(String message) {
        //todo code von Kotlin in Java !!!
    /*
    var result = Constants.DEFAULT_VALUE

        val separator = Constants.MESSAGE_SEPARATOR
        val limit = message.length-MessageIdentifier.descriptorLength //- 1
        var i = 0
        var j = 0
        var accumulator = ""
        while (i < limit){
            j=i
            val sepLimit = i+MessageIdentifier.descriptorLength
            accumulator=""
            // mistake?
            while (j < sepLimit){
                accumulator+=message[j]
                j++
            }
            if (accumulator.equals(separator)){

                var idcounter = 0
                val idLimit = i//-1
                var idDescription = ""
                while (idcounter<idLimit){
                    idDescription+=message[idcounter]
                    idcounter++
                }

                var messageCounter = sepLimit
                val mcLimit = message.length
                var messageContent = ""
                while (messageCounter<mcLimit){
                    messageContent+=message[messageCounter]
                    messageCounter++
                }

                var exists = false
                for (identifier in MessageIdentifier.values()){
                    if (identifier.descriptor.equals(idDescription)){
                        exists=true
                        break
                    }
                }
                if (exists){
                    // apply new message
                    currentMessageID = idDescription
                    currentMessage = messageContent
                    isEmpty=false
                }
                //i=limit
                break

            }else{
                i++
            }
        }

    */
    }

    public void sendMessage() {

    }

    public void stringLister() {

    }

    public void verifyMessageValidity() {

    }

    public void readMessage() {

    }

    public void setGameWebSocketListener(WebSocketListener gameWebSocketListener) {
        this.gameWebSocketListener = gameWebSocketListener;
    }
}
