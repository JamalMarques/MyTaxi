package com.mytaxi.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mytaxi.app.R;
import com.mytaxi.app.listeners.RecyclerViewListener;
import com.mytaxi.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    private boolean isLoading = false;
    private boolean lastPageLoaded = false;
    private int lastPositionAnimated = -1;
    private RecyclerViewListener<User> listener = (view, position, user) -> { /*Empty*/};

    /*Data*/
    private List<User> storeList;
    private List<User> renderingList = new ArrayList<>();

    /*Search*/
    private boolean isOnSearchingState = false;

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.storeList = userList;
    }

    public void setOnSearchingState(boolean isOnSearchingState) {
        this.isOnSearchingState = isOnSearchingState;
    }

    public boolean isOnSearchingState() {
        return isOnSearchingState;
    }

    public void setListener(RecyclerViewListener<User> listener) {
        this.listener = listener;
    }

    public RecyclerViewListener getListener() {
        return listener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPageLoaded() {
        return lastPageLoaded;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setLastPageLoaded(boolean lastPageLoaded) {
        this.lastPageLoaded = lastPageLoaded;
    }

    public void addItemsGrid(List<User> usersList) {
        storeList.addAll(usersList);
        renderingList.clear();
        renderingList.addAll(storeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item_recycler_layout, parent, false),
                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = renderingList.get(position);

        holder.tvUserName.setText(user.getLogin().getUsername());

        Glide.with(context)
                .load(user.getPicture().getThumbnail())
                .into(holder.ivUser);

        setAnimation(holder.cardLayout, position);
    }

    @Override
    public int getItemCount() {
        return renderingList.size();
    }

    public void refreshSearch(String searchText) {
        /*Clear render data*/
        renderingList.clear();
        lastPositionAnimated = -1;

        /*State*/
        isOnSearchingState = !searchText.isEmpty();

        /*Check search & populate*/
        if (isOnSearchingState) {
            /*Populate*/
            for (User user : storeList) {
                if (user.getLogin().getUsername().contains(searchText)) {
                    renderingList.add(user);
                }
            }
        } else {
            /*Reset*/
            renderingList.addAll(storeList);
        }

        /*Refresh*/
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardLayout;
        TextView tvUserName;
        ImageView ivUser;

        public ViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);

            cardLayout = itemView.findViewById(R.id.card_layout);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            ivUser = itemView.findViewById(R.id.iv_user);

            cardLayout.setOnClickListener(v -> {
                if (listener != null)
                    listener.recyclerViewOnItemClickListener(v, getLayoutPosition(), renderingList.get(getLayoutPosition()));
            });
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        ((ViewHolder) holder).clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPositionAnimated) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_animation);
            viewToAnimate.startAnimation(animation);
            lastPositionAnimated = position;
        }
    }

}
