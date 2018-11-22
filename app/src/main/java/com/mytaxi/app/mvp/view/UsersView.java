package com.mytaxi.app.mvp.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.mytaxi.app.R;
import com.mytaxi.app.activities.GenericActivity;
import com.mytaxi.app.adapters.UsersAdapter;
import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.fragments.UserDetailsFragment;
import com.mytaxi.app.models.User;
import com.mytaxi.app.models.dataShare.UserDataContainer;
import com.mytaxi.app.mvp.contract.UsersContract;
import com.mytaxi.app.utils.BusProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersView extends BaseView implements UsersContract.View {

    @BindView(R.id.et_users)
    public EditText etUsers;
    @BindView(R.id.users_recycler)
    public RecyclerView usersRecycler;
    @BindView(R.id.loading_animation)
    public LottieAnimationView lottieAnimationView;
    @BindView(R.id.pagination_loading_layout)
    public ViewGroup paginationLoadingLayout;

    private UsersAdapter usersAdapter;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            GridLayoutManager layoutManager = (GridLayoutManager) usersRecycler.getLayoutManager();
            UsersAdapter adapter = (UsersAdapter) usersRecycler.getAdapter();

            int childCount = layoutManager.getChildCount();
            int itemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!adapter.isLoading() && !adapter.isLastPageLoaded() && !adapter.isOnSearchingState()
                    && (childCount + firstVisibleItemPosition >= itemCount - 1)) {
                adapter.setLoading(true);
                post(new LoadNextPageEvent());
            }
        }
    };

    public UsersView(BaseActivity activity, BusProvider.Bus bus) {
        super(activity, bus);

        ButterKnife.bind(this, activity);

        /*Setups*/
        setupSearcher();
        setupRecycler();
    }

    private void setupSearcher() {
        etUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*Nothing to do here*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usersAdapter.refreshSearch(etUsers.getText().toString());

                if (usersAdapter.isOnSearchingState()) {
                    usersRecycler.clearOnScrollListeners();
                } else {
                    usersRecycler.addOnScrollListener(scrollListener);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*Nothing to do here*/
            }
        });
    }

    private void setupRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        usersRecycler.setLayoutManager(gridLayoutManager);
        usersAdapter = new UsersAdapter(getContext(), new ArrayList<>());
        usersAdapter.setListener((view, position, user) -> post(new OnItemClickEvent(user)));
        usersRecycler.setAdapter(usersAdapter);
    }

    @Override
    public void addItemsGrid(List<User> users) {
        usersAdapter.addItemsGrid(users);
    }

    @Override
    public void showUserDetails(UserDataContainer dataContainer) {
        Intent intent = new Intent(getContext(), GenericActivity.class);
        intent.putExtra(GenericActivity.FRAGMENT_TAG, UserDetailsFragment.TAG);
        intent.putExtra(UserDetailsFragment.USER_DATA, dataContainer);
        getContext().startActivity(intent);
    }

    @Override
    public void showApiErrorSnackbar() {
        Snackbar.make(getActivity().getCoordinatorLayout(), R.string.network_error_message, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void setLoadingState(boolean isLoading) {
        usersAdapter.setLoading(isLoading);

        if (isLoading && !lottieAnimationView.isAnimating()) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        } else {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.cancelAnimation();
        }

        usersRecycler.setVisibility((isLoading) ? View.GONE : View.VISIBLE);
        usersRecycler.addOnScrollListener(scrollListener);
    }

    @Override
    public void setLoadingStatePagination(boolean isLoading) {
        paginationLoadingLayout.setVisibility((isLoading) ? View.VISIBLE : View.GONE);
    }
}
