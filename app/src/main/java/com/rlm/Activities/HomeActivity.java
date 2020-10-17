package com.rlm.Activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rlm.Dialog.ProgressDialog;
import com.rlm.R;
import com.rlm.databinding.ActivityHomeBinding;
import com.utils.Session.SessionKey;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home);
        Tools.get().updateView(this, binding.getRoot());
        BindView();
    }

    private void BindView() {
        session= SessionManager.get(this);
        NavController navControler = Navigation.findNavController(this,R.id.nav_host_home);
        binding.navDrawer.setViewScale(Gravity.START, 0.8f);
        binding.navDrawer.setViewElevation(Gravity.START, 20);
        binding.navDrawer.setRadius(GravityCompat.START, 25);
        binding.imgMenu.setOnClickListener(v->{
            binding.navDrawer.openDrawer(GravityCompat.START);
        });
       binding.tvWhatAboutUs.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
           navControler.navigate(R.id.nav_about_us);
       });
       binding.tvMain.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
          navControler.navigate(R.id.nav_home);
       });
       binding.tvCallUs.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
          navControler.navigate(R.id.nav_contact_us);
       });
       binding.tvOurService.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
          navControler.navigate(R.id.nav_service);
       });
       binding.tvStatistics.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
          navControler.navigate(R.id.nav_statistics);
       });binding.tvAppointmentBooking.setOnClickListener(v->{
           binding.navDrawer.closeDrawer(GravityCompat.START);
          navControler.navigate(R.id.nav_appointment);
       });
        binding.tvLogout.setOnClickListener(v->{
            binding.navDrawer.closeDrawer(GravityCompat.START);
           session.Logout();
           startActivity(new Intent(this,FirstActivity.class));
           finish();
        });
       binding.spinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String lastLng=session.getSelectedLanguage();
               switch (position){
                   case 1:
                       session.setLanguage("en");
                       break;
                   case 2:
                       session.setLanguage("ar");
                       break;
               }
               if (!lastLng.equals(session.getSelectedLanguage())){
                   startActivity(getIntent());
                   finish();
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       binding.imgNotification.setOnClickListener(v->{
           navControler.navigate(R.id.nav_notification);
       });
       setData();
    }

    private void setData() {
        binding.tvUserName.setText(session.getValue(SessionKey.first_name)+" "+session.getValue(SessionKey.last_name));
        binding.tvLanguage.setText(getString(session.getSelectedLanguage().equals("en")?R.string.english:R.string.arabic));
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            Log.e("FirebaseToken","===>"+instanceIdResult.getToken());
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
