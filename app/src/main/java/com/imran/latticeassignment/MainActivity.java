package com.imran.latticeassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imran.latticeassignment.database.Database;
import com.imran.latticeassignment.database.MyDao;
import com.imran.latticeassignment.fragment.RegisterFragment;
import com.imran.latticeassignment.model.PincodeList;
import com.imran.latticeassignment.model.PostOffice;
import com.imran.latticeassignment.model.User;
import com.imran.latticeassignment.network.ApiClient;
import com.imran.latticeassignment.network.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RegisterFragment registerFragment= new RegisterFragment();
        addFragment(R.id.container_FL,registerFragment);



    }





    public void addFragment(int replaceId, Fragment fragment) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(replaceId, fragment);
            ft.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void replaceFragment(int replaceId, Fragment replaceFragment)
    {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(replaceId,replaceFragment);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
















}