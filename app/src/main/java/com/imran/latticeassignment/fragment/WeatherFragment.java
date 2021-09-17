package com.imran.latticeassignment.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.imran.latticeassignment.Constant;
import com.imran.latticeassignment.R;
import com.imran.latticeassignment.model.WeatherList;
import com.imran.latticeassignment.network.ApiClient;
import com.imran.latticeassignment.network.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

private TextView city,temp,mood;
private ImageView image;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_weather, container, false);
        initview(view);

        Hitapi();
        return view;
    }

    private void initview(View view) {
        city=view.findViewById(R.id.city);
        temp=view.findViewById(R.id.temp);
        mood=view.findViewById(R.id.mood);
        image=view.findViewById(R.id.image);
        progressBar=view.findViewById(R.id.progressBar);
    }

    private void Hitapi() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient(Constant.URL2);
        Call<WeatherList> call = apiInterface.getweather(Constant.KEY,"katihar");
        call.enqueue(new Callback<WeatherList>() {
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {
                if (response.isSuccessful())
                {
                    Log.e("bundle",getArguments().getString(Constant.city));
                    Log.e("image",response.body().getCurrent().getCondition().getIcon());
                    city.setText(getArguments().getString(Constant.city));
                    temp.setText(String.valueOf(response.body().getCurrent().getTempC()));
                    mood.setText(response.body().getCurrent().getCondition().getText());

                    Picasso.with(getActivity())
                            .load("https:"+response.body().getCurrent().getCondition().getIcon())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(image);
                    progressBar.setVisibility(View.INVISIBLE);

                    Log.e("responsewhether",""+response.body().getCurrent().getTempC());
                }
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable t) {

            }
        });
    }
}