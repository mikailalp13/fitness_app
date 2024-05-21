package com.example.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitness_app.fragments.anasayfa;
import com.example.fitness_app.fragments.egzersiz;
import com.example.fitness_app.fragments.yemek;
import com.example.fitness_app.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnb;
    TextView kullaniciadi, textViewAlinanKalori;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private int totalCaloriesConsumed = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnb = findViewById(R.id.bnb);
        textViewAlinanKalori = findViewById(R.id.textViewAlinanKalori);
        kullaniciadi = findViewById(R.id.kullaniciadi);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    kullaniciadi.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Veri çekme hatası: " + error.getMessage());
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fla, new anasayfa()).commit();
        }

        bnb.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;

                if (id == R.id.anasayfa) {
                    selectedFragment = new anasayfa();
                } else if (id == R.id.egzersiz) {
                    selectedFragment = new egzersiz();
                } else if (id == R.id.yemek) {
                    selectedFragment = new yemek();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fla, selectedFragment).commit();
                    Log.d("MainActivity", "Fragment replaced: " + selectedFragment.getClass().getSimpleName());
                } else {
                    Log.e("MainActivity", "Error replacing fragment: Fragment is null");
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void updateConsumedCalories(int calories) {
        totalCaloriesConsumed += calories;
        textViewAlinanKalori.setText(String.format("Toplam Alınan Kalori: %d", totalCaloriesConsumed));

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fla);
        if (currentFragment instanceof anasayfa) {
            ((anasayfa) currentFragment).updateTotalCalories(totalCaloriesConsumed);
        }
    }
}
