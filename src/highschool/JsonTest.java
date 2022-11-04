/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highschool;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FRED
 */
public class JsonTest {
    public static void main(String[] args) {
        ArrayList senditems = new ArrayList();
        Map<Object, Object> sendData = new HashMap();

        Map<Object, Object> messageparameter = new HashMap();
        Map<String, Object> recipient1 = new HashMap();
        Map<String, Object> recipient2 = new HashMap();

        recipient1.put("message", "hello");
        recipient1.put("phone", "0707353225");
        recipient2.put("message", "hello 2");
        recipient2.put("phone", "072563945");
        senditems.add(recipient1);
        senditems.add(recipient2);
        sendData.put("secretKey", "fred@2020!");
        sendData.put("senderid", "22136");
        sendData.put("messageParameters", senditems);


        Gson gson = new Gson();
        System.err.println(gson.toJson(sendData));

    }


}
