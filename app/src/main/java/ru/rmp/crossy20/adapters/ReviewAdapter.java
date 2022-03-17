package ru.rmp.crossy20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.models.Book;
import ru.rmp.crossy20.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.inflater = LayoutInflater.from(context);
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.nicknameTextView.setText(review.getNickname());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView contentTextView;
        final TextView nicknameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.item_review_content_textview);
            nicknameTextView = itemView.findViewById(R.id.item_review_nickname_textview);
        }
    }
}
