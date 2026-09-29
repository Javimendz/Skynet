package com.example.skynet.ui.test;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.skynet.R;
import com.example.skynet.data.remote.StompManager;
import com.example.skynet.ui.main.FriendAdapter;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

public class MockStompActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // This is a helper activity to trigger mock updates if needed, 
        // but it's better to just add a "Mock" button in DesarrolloActivity temporarily or use a long press.
    }
}
