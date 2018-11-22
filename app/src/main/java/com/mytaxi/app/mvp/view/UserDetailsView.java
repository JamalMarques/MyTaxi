package com.mytaxi.app.mvp.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mytaxi.app.R;
import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.mvp.contract.UserDetailsContract;
import com.mytaxi.app.utils.BusProvider;
import com.mytaxi.app.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsView extends BaseView implements UserDetailsContract.View {

    @BindView(R.id.iv_user)
    public ImageView ivUser;
    @BindView(R.id.tv_user_first_name)
    public TextView tvUserFirstName;
    @BindView(R.id.tv_user_last_name)
    public TextView tvUserLastName;
    @BindView(R.id.tv_email)
    public TextView tvEmail;
    @BindView(R.id.tv_address)
    public TextView tvAddress;

    public UserDetailsView(BaseActivity activity, BusProvider.Bus bus) {
        super(activity, bus);

        ButterKnife.bind(this, activity);
    }

    @Override
    public void showUserData(String image, String username, String firstName, String lastName, String email, String address) {

        Glide.with(getContext())
                .load(image)
                .into(ivUser);

        tvUserFirstName.setText(TextHelper.textToUpperCase(firstName));
        tvUserLastName.setText(TextHelper.textToUpperCase(lastName));
        tvEmail.setText(email);
        tvAddress.setText(address);
    }
}
