package com.example.firebaseone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import com.example.firebaseone.ContactActivity;

public class HomeFragment extends Fragment {

    private String TAG="";
    private String userID;

    private String contactOne;
    private String contactTwo;
    private String contactThree;

    //Firebase vars
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        userID=user.getUid();
        myRef=myRef.child(userID);

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                    ToastMessage("Successfully signed in with :"+user.getEmail());
                }else {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                    ToastMessage("Successfully signed out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactOne=dataSnapshot.child("firstPhone").getValue().toString();
                contactTwo=dataSnapshot.child("secondPhone").getValue().toString();
                contactThree=dataSnapshot.child("thirdPhone").getValue().toString();
                Log.d(TAG,contactOne+", "+contactTwo+", "+contactThree);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener!=null){
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void ToastMessage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

}
