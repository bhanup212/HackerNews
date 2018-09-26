package bhanupro.hackernews.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import bhanupro.hackernews.MainActivity;
import bhanupro.hackernews.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.google_sign_in_button) SignInButton signinButton;

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //Firebase Auth object
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        signinButton.setSize(SignInButton.SIZE_STANDARD);

        //intialize the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //initialize the googleSignIn Object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


    }
    // This method will fire when google sign in button clicked
    @OnClick(R.id.google_sign_in_button)
    public void onSignInButtonClicked(){
        if (mAuth.getCurrentUser()== null){
            Intent i = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(i,RC_SIGN_IN);
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithgoogle(account);

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void firebaseAuthWithgoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SplashActivity.this,"Sign in successful",Toast.LENGTH_LONG).show();

                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(SplashActivity.this,"Sign in failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
