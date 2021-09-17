package com.imran.latticeassignment.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.imran.latticeassignment.Constant;
import com.imran.latticeassignment.MainActivity;
import com.imran.latticeassignment.R;
import com.imran.latticeassignment.database.Database;
import com.imran.latticeassignment.database.MyDao;
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

public class RegisterFragment extends Fragment {

    private EditText Name,Mobile,Dob,Address1,Address2, Pincode,Age,State,District;
    private Spinner Gender;
    private Button pinbutton,register;
    private MyDao db;
    private int pincode;
    private ArrayList<PostOffice> pincodeLists;
    private ProgressBar progressBar;
    private MainActivity mainActivity;

    private void updateDate() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        Dob.setText(simpleDateFormat.format(mycalendar.getTime()));

        String dob=simpleDateFormat.format(mycalendar.getTime());
        Date dobdate= null;
        try {
            dobdate = simpleDateFormat.parse(dob);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Age.setText("Age:- "+String.valueOf(calculateAge(dobdate)));
        Log.e("dobcal", String.valueOf(calculateAge(dobdate)));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_register, container, false);

        db= Room.databaseBuilder(getActivity(), Database.class,"Lattice")
                .allowMainThreadQueries()
                .build().getUserDao();
        initview(view);
        spinner();
        dob();
        return view;
    }
    final Calendar mycalendar= Calendar.getInstance();

    private void initview(View view) {
        Name=view.findViewById(R.id.etfullname);
        Mobile=view.findViewById(R.id.etmobileno);
        Dob=view.findViewById(R.id.etdob);
        Address1=view.findViewById(R.id.etaddress1);
        Address2=view.findViewById(R.id.etaddress2);
        Pincode =view.findViewById(R.id.etpincode);
        Gender=view.findViewById(R.id.etgender);
        Age=view.findViewById(R.id.etage);
        State=view.findViewById(R.id.etstate);
        District=view.findViewById(R.id.etdistrict);
        pinbutton=view.findViewById(R.id.pinbtn);
        register=view.findViewById(R.id.btnRegister);
        progressBar=view.findViewById(R.id.progressBar);
        mainActivity = (MainActivity) getActivity();
        pincodeLists=new ArrayList<>();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    nextfragment();
                }



            }
        });

            pinbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Pincode.getText().toString().length()>5){
                        progressBar.setVisibility(View.VISIBLE);
                        pincode=Integer.valueOf(Pincode.getText().toString());
                        Hitapi();
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(getView(), "Please Enter pincode 👈👈", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
            });



    }



    private void spinner() {

        String [] gender={"Choose Gender","Male","Female"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,gender);
        Gender.setAdapter(adapter);

    }
    private boolean validateData() {

        if (Name.getText().length()== Constant.ZERO)
        {
            settingError(Name,getString(R.string.name_empty_error));
        }
        else if (Mobile.getText().length()==Constant.ZERO)
        {
            settingError(Mobile,getString(R.string.phone_empty_error));
        }
        else if (Gender.getSelectedItem()=="Choose Gender")
        {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please Choose Gender", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (Dob.getText().length()==Constant.ZERO)
        {
            settingError(Dob,getString(R.string.dob_empty_error));
        }
        else if (Address1.getText().length()==Constant.ZERO)
        {
            settingError(Address1,getString(R.string.address_empty_error));
        }
        else if (Address1.getText().length()<Constant.FOUR)
        {
            settingError(Address1,getString(R.string.address_valid_error));
        }
        else return true;

        return false;

    }
    private void nextfragment() {
        String fullname,mobileno,age,gender,address1,address2,state,district;
        int pincode;
        fullname=Name.getText().toString();
        mobileno=Mobile.getText().toString();
        age=Age.getText().toString();
        gender=Gender.getSelectedItem().toString();
        address1=Address1.getText().toString();
        address2=Address2.getText().toString();
        state=State.getText().toString();
        district=District.getText().toString();
        savedata(fullname,mobileno,age,gender,address1,address2,state,district);
        if (district.length()>0)
        {
            WeatherFragment weatherFragment=new WeatherFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constant.city,district);
            weatherFragment.setArguments(bundle);
            mainActivity.replaceFragment(R.id.container_FL,weatherFragment);
        }else
        {

            Snackbar snackbar = Snackbar
                    .make(getView(), "Please click on  pincode !!!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }
    private void settingError(EditText editText, String validationText) {
        editText.requestFocus();
        editText.setError(validationText);
    }
    private void savedata(String fullname,String mobileno,String age,String gender,String address1, String address2, String state, String district) {
        User user = new User(fullname,mobileno,age,gender,address1,address2,state,district);

        db.insert(user);
        if (db.getUser()!=null)
        {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Register Succesfully 👍", Snackbar.LENGTH_LONG);
            snackbar.show();
            User user1=db.getUser();
            Log.e("getdata",""+user1.getFullname());
        }
        else{
            Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void Hitapi() {
        ApiInterface apiInterface= ApiClient.getClient(Constant.URL1);
        Call<List<PincodeList>> call= apiInterface.getpincode(pincode);
        call.enqueue(new Callback<List<PincodeList>>() {
            @Override
            public void onResponse(Call<List<PincodeList>> call, Response<List<PincodeList>> response) {
                if (response.isSuccessful())
                {
                    if (response.body().get(Constant.ZERO).getPostOffice()!=null)
                    pincodeLists=response.body().get(Constant.ZERO).getPostOffice();
                    District.setText(pincodeLists.get(Constant.ZERO).getDistrict());
                    State.setText(pincodeLists.get(Constant.ZERO).getState());
                    progressBar.setVisibility(View.GONE);
                    Log.e("responsecheck",""+pincodeLists.get(Constant.ZERO).getState());
                }
            }

            @Override
            public void onFailure(Call<List<PincodeList>> call, Throwable t) {
                Log.e("responsefailure",t.getMessage());
            }
        });
    }

    private void dob() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateDate();
            }
        };

        Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,
                        mycalendar.get(Calendar.YEAR),mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    public  int calculateAge(Date birthdate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int yearDifference = today.get(Calendar.YEAR)
                - birth.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
            yearDifference--;
        } else {
            if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birth
                    .get(Calendar.DAY_OF_MONTH)) {
                yearDifference--;
            }

        }

        return yearDifference;
    }
}

