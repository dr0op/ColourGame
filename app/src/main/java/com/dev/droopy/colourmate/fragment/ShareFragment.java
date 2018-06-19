package com.dev.droopy.colourmate.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dev.droopy.colourmate.MainActivity;
import com.dev.droopy.colourmate.R;
import com.dev.droopy.colourmate.RequestHandler;
import com.dev.droopy.colourmate.gameClassicActivity;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Admin on 07-08-2017.
 */

public class ShareFragment extends Fragment implements View.OnClickListener {
    private EditText feed;
    private ImageButton rate, share;
    private Button send;
    ProgressDialog loading;
    String feedback;

    public ShareFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        feed = (EditText) view.findViewById(R.id.feedback);
        share = (ImageButton) view.findViewById(R.id.share);
        rate = (ImageButton) view.findViewById(R.id.rate);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(this);
        feed.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        feedback = feed.getText().toString();


        getActivity().setTitle("Instruction");
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            String possibleEmail = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    //showing dialog to select image

                    Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                    Account[] accounts = AccountManager.get(getActivity()).getAccounts();
                    for (Account account : accounts) {
                        if (emailPattern.matcher(account.name).matches()) {
                            possibleEmail = account.name;
                        }
                    }

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.GET_ACCOUNTS,
                                    android.Manifest.permission.CAMERA}, 1);
                }
            } else {


                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(getActivity()).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        possibleEmail = account.name;
                    }
                }
            }
            feedback = feed.getText().toString();
            if(feedback.isEmpty()){
                feed.setError("Can't be left empty");
                return;
            }
            uploadfeed(possibleEmail, feedback);
        }
        if (v == share) {
            final String appPackageName = getActivity().getPackageName();
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "ColorMate");
                String sAux = "Color Clash is the game can help you train agility, reflexes extremely high.\n" +
                        "Can you differentiate colors very fast? Try this one!\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName;
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }
        if (v == rate) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Rate");
            alertDialog.setMessage("Rate us 5 star ");
            alertDialog.setIcon(R.drawable.rate);
            alertDialog.show();
            rateus();

        }
    }

    private void uploadfeed(final String feedemail, final String feedback) {

        class Uploadfeed extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Sending...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                feed.setText("Thank you for your feedback");
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> data = new HashMap<>();

                data.put("from", feedemail);
                data.put("message", feedback);
                RequestHandler rh = new RequestHandler();
                String result = rh.sendPostRequest("https://unsubstantial-devia.000webhostapp.com/feedback.php", data);

                return result;
            }
        }

        Uploadfeed ui = new Uploadfeed();
        ui.execute();
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getActivity().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
    private void rateus(){
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }
}

