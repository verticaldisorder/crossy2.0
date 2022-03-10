package ru.rmp.crossy20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.models.Book;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Book> books;

    public LibraryAdapter(Context context, List<Book> books) {
        this.inflater = LayoutInflater.from(context);
        this.books = books;
    }

    @NonNull
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_book_in_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.authorView.setText(book.getAuthor());
        holder.titleView.setText(book.getTitle());
        holder.genreView.setText(book.getGenre());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView authorView;
        final TextView titleView;
        final TextView genreView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorView = itemView.findViewById(R.id.item_library_author_textview);
            titleView = itemView.findViewById(R.id.item_library_title_textview);
            genreView = itemView.findViewById(R.id.item_library_genre_textview);
        }
    }
}
