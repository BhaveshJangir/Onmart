package com.example.onmart.ui.AccountInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.onmart.Database.OrderDatabase;
import com.example.onmart.R;
import com.example.onmart.RegisterActivity;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {


    OrderDatabase orderDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        orderDatabase = OrderDatabase.getDb(getContext());
        ImageView mProfile = (ImageView) rootView.findViewById(R.id.profile_info);
        TextView mUsernameTv = (TextView) rootView.findViewById(R.id.username_info);
        TextView mEmailTv = (TextView) rootView.findViewById(R.id.email_info);
        EditText mUsername = (EditText) rootView.findViewById(R.id.name_info_et);
        EditText mEmail = (EditText) rootView.findViewById(R.id.email_info_et);
        EditText mMobile = (EditText) rootView.findViewById(R.id.mobile_info_et);
        EditText mAddress = (EditText) rootView.findViewById(R.id.address_info_et);
        Button mBackBt = (Button) rootView.findViewById(R.id.back_bt);

    //    Picasso.get().load(orderDatabase.userDao().getUserData().get(0).getImage()).into(mProfile);
        mUsernameTv.setText(orderDatabase.userDao().getUserData().get(0).getUserName());
        mUsername.setText(orderDatabase.userDao().getUserData().get(0).getUserName());
        mEmail.setText(orderDatabase.userDao().getUserData().get(0).getEmail());
        mEmailTv.setText(orderDatabase.userDao().getUserData().get(0).getEmail());
        //Toast.makeText(getContext(), orderDatabase.userDao().getUserData().get(1).getEmail()+"kshfkshkfh", Toast.LENGTH_SHORT).show();
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(), RegisterActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}