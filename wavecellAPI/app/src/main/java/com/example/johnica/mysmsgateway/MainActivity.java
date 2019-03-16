package com.example.johnica.mysmsgateway;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    Button send;
    EditText number, message1;

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send=findViewById(R.id.buttonSend);
        message1=findViewById(R.id.inputMessage);
        number=findViewById(R.id.inputNumber);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Construct data


                    String apiKey = "apikey=" + "V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs";
                    String text = "&text=" + message1.getText().toString();
                    String source = "&source=" + "Take Care";
                    String destination = "&destination=" + number.getText().toString();

                    // Send data
                   /* HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://posttestserver.com/post.php");
                    httppost.setHeader("Authorization", "Bearer "+ V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs);
                    httppost.addHeader("Authorization: Bearer", V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs);*/

                   /* HttpURLConnection postRequest = new HttpURLConnection("https://api.wavecell.com/sms/v1/TAKECARE_0kKCF_hq/single");
                     Setting the Authorization Header values
                    postRequest.setHeader("Authorization",  "Bearer V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs");*/

                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.wavecell.com/sms/v1/TAKECARE_0kKCF_hq/single").openConnection();
                    conn.setHeader("Authorization", "Bearer V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs");
                    conn.DefaultRequestHeaders
                setRequestHeader('Content-Type','application/json');
                            conn.setRequestHeader("Authorization", "Bearer {V2yRl8sKSVV0sceRB2FFc5y0dnssx1JI0sIVzlNffIs}");


                    String data = apiKey + destination + text + source;

                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Toast.makeText(MainActivity.this,line.toString(), Toast.LENGTH_SHORT).show();
                    }
                    rd.close();


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        StrictMode.ThreadPolicy st= new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(st);

    }
}