package me.silverandroid.pebblelocalize;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;

import me.silverandroid.pebblelocalize.retrofit.Group;
import me.silverandroid.pebblelocalize.retrofit.LocalizeClient;
import me.silverandroid.pebblelocalize.retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (TempData.getInstance().isInGroup()) {
            acceptedGroup();
        } else {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme)
                                .setView(R.layout.dialog_create_group)
                                .setTitle("Create Group")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Dialog view = (Dialog) dialog;
                                        EditText nameView = (EditText) view.findViewById(R.id.name);
                                        EditText latitudeView = (EditText) view.findViewById(R.id.latitude);
                                        EditText longitudeView = (EditText) view.findViewById(R.id.longitude);

                                        String name = nameView.getText().toString();
                                        float latitude = Float.parseFloat(latitudeView.getText().toString());
                                        float longitude = Float.parseFloat(longitudeView.getText().toString());
                                        LocalizeClient client = ServiceGenerator.createService(LocalizeClient
                                                .class);
                                        Call<Group> createGroupRequest = client.createGroup(TempData
                                                .getInstance().getUserID(), name, latitude, longitude);
                                        createGroupRequest.enqueue(new Callback<Group>() {
                                            public static final String TAG = "CreateGroupRequest";

                                            @Override
                                            public void onResponse(Call<Group> call, Response<Group> response) {
                                                Log.i(TAG, "onResponse: successful response");
                                                int statusCode = response.code();
                                                Log.d(TAG, "onResponse: " + statusCode);
                                                if (statusCode == 200) {
                                                    TempData.getInstance().setCoordinates(response.body()
                                                            .getDestLat(), response.body().getDestLong());
                                                    acceptedGroup();
                                                } else {
                                                    Toast.makeText(MainActivity.this, response.body()
                                                            .getResponse(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Group> call, Throwable t) {
                                                Log.e(TAG, "onFailure: failed to create group", t);
                                            }
                                        });
                                    }
                                }).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isConnected = PebbleKit.isWatchConnected(this);
        Toast.makeText(this, "Pebble " + (isConnected ? "is " : "is not ") + "connected!", Toast
                .LENGTH_SHORT).show();
    }

    private void acceptedGroup() {
        Intent intent = new Intent(MainActivity.this, DirectionActivity.class);
        startActivity(intent);
        finish();
    }
}
