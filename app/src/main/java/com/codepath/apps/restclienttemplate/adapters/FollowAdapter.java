package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    public List<User> users;
    Context context;

    public FollowAdapter(Context context, List<User> users) {
        this.context = context;
        Log.d("FollowAdapter", "got an array list user with " + users.size());
        this.users = users;
    }


    @NonNull
    @NotNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView name;
        TextView description;
        ImageView ivProfileImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tvUser);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescription);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        }

        public void bind(User user) {
            Log.d("FollowAdapter", "binding user with " + user.id);
            userName.setText(user.screenName);
            name.setText(user.name);
            description.setText(user.description);
            Glide.with(context)
                    .load(user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(ivProfileImage);
        }
    }
}
