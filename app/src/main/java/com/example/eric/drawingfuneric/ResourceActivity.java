package com.example.eric.drawingfuneric;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class ResourceActivity extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference button = findPreference("gitHub");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fraziere3"));
                startActivity(browserIntent);
                return true;
            }
        });

        Preference button2 = findPreference("colorLibrary");
        button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference){
                    Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yukuku/ambilwarna"));
                    startActivity(browserIntent2);

                return true;
            }
        });
    }
    //public void onPreferenceClickListener()
}