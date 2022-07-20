package com.example.parser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {

    Button parsexml, parsejson;
    TextView displayxml,displayJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayxml = findViewById(R.id.r_display);
        displayJson = findViewById(R.id.r1_display);
        parsexml = findViewById(R.id.b_xml);
        parsejson = findViewById(R.id.b_json);

        parsexml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream is = getAssets().open("city.xml");
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(is);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("XML DATA");
                    stringBuilder.append("\n---------");
                    NodeList nodeList = document.getElementsByTagName("place");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            stringBuilder.append("\nName: ").append(getValue("name", element));
                            stringBuilder.append("\nLatitude: ").append(getValue("lat", element));
                            stringBuilder.append("\nLongitude: ").append(getValue("long", element));
                            stringBuilder.append("\nTemperature: ").append(getValue("temperature", element));
                            stringBuilder.append("\nHumidity: ").append(getValue("humidity", element));
                        }
                    }
                    displayxml.setText(stringBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error Parsing XML", Toast.LENGTH_LONG).show();
                }
            }

        });


        parsejson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    InputStream is = getAssets().open("example.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    json = new String(buffer, StandardCharsets.UTF_8);  //ordering
                    JSONArray jsonArray = new JSONArray(json);
                    stringBuilder.append("JSON DATA");
                    stringBuilder.append("\n--------");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        stringBuilder.append("\nName: ").append(jsonObject.getString("name"));
                        stringBuilder.append("\nLatitude: ").append(jsonObject.getString("lat"));
                        stringBuilder.append("\nLongitude: ").append(jsonObject.getString("long"));
                        stringBuilder.append("\nTemperature: ").append(jsonObject.getString("temperature"));
                        stringBuilder.append("\nHumidity: ").append(jsonObject.getString("humidity"));
                    }
                    displayJson.setText(stringBuilder.toString());
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error in reading", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private String getValue(String tag, Element element)
    {
        return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();

    }

}