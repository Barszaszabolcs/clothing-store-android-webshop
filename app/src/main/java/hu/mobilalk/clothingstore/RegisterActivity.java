package hu.mobilalk.clothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 99;
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    EditText phoneEditText;
    EditText addressEditText;
    RadioGroup genderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99){
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        genderGroup = findViewById(R.id.genderGroup);
        genderGroup.check(R.id.buyerRadioButton);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userEmail = preferences.getString("userEmail", "");
        String password = preferences.getString("password", "");

        emailEditText.setText(userEmail);
        passwordEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        int checkedId = genderGroup.getCheckedRadioButtonId();
        RadioButton radioButton = genderGroup.findViewById(checkedId);
        String gender = radioButton.getText().toString();

        if(!email.matches("^[A-Za-z0-9]{3,}[@][a-z]{4,}[.][a-z]{2,}$")) {
            Toast.makeText(RegisterActivity.this, "Az e-mail cím formátuma nem megfelelő!", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(passwordAgain)) {
            Toast.makeText(RegisterActivity.this, "A két jelszó nem egyezik!", Toast.LENGTH_LONG).show();
            return;
        }
        if(userName.equals("") || email.equals("") || password.equals("") || passwordAgain.equals("") || phone.equals("") || address.equals("") || gender.equals("")) {
            Toast.makeText(RegisterActivity.this, "Minden mezőt ki kell tölteni!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ShopListActivity.class);
                intent.putExtra("SECRET_KEY", SECRET_KEY);

                finish();

                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "A felhasználó létrehozása nem sikerült: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}