package in.icebreakerapp.icebreaker;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.icebreakerapp.icebreaker.adapters.ChatAdapter;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;
import in.icebreakerapp.icebreaker.models.SendMessage;

/**
 * Created by siddharth on 23-08-2016.
 */
public class ChatActivity extends ActionBarActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    public static ChatAdapter adapter;
    Intent serviceIntent;
    BroadcastReceiver receiver;
    public static List<IcebreakerNotification> chatHistory;
    MessageDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatHistory = new ArrayList<IcebreakerNotification>();
        db =new MessageDb(ChatActivity.this);

        initControls();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String time = intent.getStringExtra("time");
                String counter = intent.getStringExtra("counter");
                loadDummyHistory();
                scroll();


            }
        };
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
//        companionLabel.setText("My Buddy");// Hard Coded
        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                String serverURL1 = "http://anip.xyz/icebreakerlogin.php";
                new LongOperation2().execute(serverURL1);


                IcebreakerNotification chatMessage = new IcebreakerNotification();
//                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
//                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });
    }

    public void displayMessage(IcebreakerNotification message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){



//        ChatMessage msg = new ChatMessage();
//        msg.setId(1);
//        msg.setMe(false);
//        msg.setMessage("Hi");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg);
//        ChatMessage msg1 = new ChatMessage();
//        msg1.setId(2);
//        msg1.setMe(false);
//        msg1.setMessage("How r u doing???");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg1);
        chatHistory = db.getTodayFoodItems();


        adapter = new ChatAdapter(ChatActivity.this, db.getTodayFoodItems());
        messagesContainer.setAdapter(adapter);

//        for(int i=0; i<chatHistory.size(); i++) {
//            IcebreakerNotification message = chatHistory.get(i);
//            displayMessage(message);
//        }
    }
    private class LongOperation2 extends AsyncTask<String, Void, SendMessage> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        // private String Content;
        private String Error = null;
        private SendMessage result;
        String studentId;
        private ProgressDialog Dialog = new ProgressDialog(ChatActivity.this);
        String data = "";

        int sizeData = 0;

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            // Start Progress Dialog (Message)
            // String studentid="";

            // Intent intent = getIntent();

            // if (intent != null) {

            // emailId = intent.getStringExtra("emailId");

            // }*/

            Dialog.setMessage("Please wait..");
            Dialog.show();
            // try{
            // Set Request parameter
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("to", "13103622");
            jsonObject.addProperty("message",messageET.getText().toString());
            jsonObject.addProperty("from", "13103630");


            Gson gson2 = new Gson();

            String jsonString = gson2.toJson(jsonObject);
            Log.i("hell", jsonString);
            // data +=
            // "{\"order\":{\"instructions\":\"\",\"paymentMethod\":\"COD\",\"items\":[{\"itemId\":962,\"name\":\"Cottage Cheese & Grilled Veggies Salad\",\"smallImageUrl\":\"/static/21/962_Cottage_Cheese_Salad200x200.jpg\",\"price\":249,\"itemType\":\"Veggies\",\"instructions\":\"instructions Abc\",\"quantity\":1},{\"itemId\":867,\"name\":\"Greek Salad\",\"smallImageUrl\":\"/static/21/867_Greek_Salad200x200.jpg\",\"price\":219,\"itemType\":\"Veggies\",\"instructions\":\"ABC\",\"quantity\":1}],\"deliveryCharges\":30,\"discountAmount\":117,\"discountPercentage\":25,\"finalOrderAmount\":397,\"discountList\":[{\"id\":4,\"name\":\"Corprate Discount\",\"category\":\"Discount\",\"type\":\"PERCENTAGE\",\"value\":25}],\"deliveryDateTime\":\"12-7-2015 19:45\"},\"customer\":{\"name\":\"hhhhh null\",\"phone\":9540095277,\"email\":\"rahul@cookedspecially.com\",\"address\":\"nvdiv eiv iwr\",\"deliveryArea\":\"DLF Phase 3\",\"city\":\"Gurgaon\",\"id\":9970}}";
            // data += "{" + "\"phoneNumber\"" + ":\"" +
            // mobileno.getText().toString()
            // + "\"}";
            data = jsonString;
            // data +=
            // "%7B%22order%22%3A%7B%22instructions%22%3A%22%22%2C%22paymentMethod%22%3A%22COD%22%2C%22items%22%3A%5B%7B%22itemId%22%3A962%2C%22name%22%3A%22Cottage+Cheese+%26+Grilled+Veggies+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F962_Cottage_Cheese_Salad200x200.jpg%22%2C%22price%22%3A249%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22instructions+Abc%22%2C%22quantity%22%3A1%7D%2C%7B%22itemId%22%3A867%2C%22name%22%3A%22Greek+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F867_Greek_Salad200x200.jpg%22%2C%22price%22%3A219%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22ABC%22%2C%22quantity%22%3A1%7D%5D%2C%22deliveryCharges%22%3A30%2C%22discountAmount%22%3A117%2C%22discountPercentage%22%3A25%2C%22finalOrderAmount%22%3A397%2C%22discountList%22%3A%5B%7B%22id%22%3A4%2C%22name%22%3A%22Corprate+Discount%22%2C%22category%22%3A%22Discount%22%2C%22type%22%3A%22PERCENTAGE%22%2C%22value%22%3A25%7D%5D%2C%22deliveryDateTime%22%3A%2212-7-2015+19%3A45%22%7D%2C%22customer%22%3A%7B%22name%22%3A%22hhhhh+null%22%2C%22phone%22%3A9540095277%2C%22email%22%3A%22rahul%40cookedspecially.com%22%2C%22address%22%3A%22nvdiv+eiv+iwr%22%2C%22deliveryArea%22%3A%22DLF+Phase+3%22%2C%22city%22%3A%22Gurgaon%22%2C%22id%22%3A9970%7D%7D";
        }

        // Call after onPreExecute method
        protected SendMessage doInBackground(String... urls) {


            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://10.0.2.2:8000/send/").openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();
                Log.i("hel", String.valueOf(httpcon.getErrorStream()) + httpcon.getResponseMessage() + httpcon.getResponseCode());


                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                Gson gson2 = new Gson();
                Log.i("he;;", sb.toString());

                result = gson2.fromJson(sb.toString(), SendMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Append Server Response To Content String


            /*****************************************************/
            return result;
        }

        protected void onPostExecute(SendMessage response) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            Log.i("response", String.valueOf(response) + Error);
            if (response.getStatus().equalsIgnoreCase("true")) {
//                Intent intent = new Intent(Lo.this, MainActivity.class);
//                startActivity(intent);
            } else {
                Toast.makeText(ChatActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();
            }


            // Show Response Json On Screen (activity)
            // uiUpdate.setText(Content);

            /****************** Start Parse Response JSON Data *************/

            // String OutputData = ""
//                               	SharedPreferences sp = getApplicationContext()


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceIntent = new Intent(getApplicationContext(),
                UpdaterService.class);
//        startService(serviceIntent);

        registerReceiver(receiver, new IntentFilter(
                UpdaterService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finishAffinity();
    }
}
