package com.dev.droopy.colourmate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.google.example.games.basegameutils.BaseGameActivity;

import com.dev.droopy.colourmate.fragment.LibraryFragment;

public class instructionGame extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_game);
        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransiction=fragmentManager.beginTransaction();
        fragment = new LibraryFragment();
        fragmentTransiction.replace(R.id.main_container_wrapper,fragment);
        fragmentTransiction.commit();



    }

}
