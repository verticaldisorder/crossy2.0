package ru.rmp.crossy20.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import ru.rmp.crossy20.R;
import ru.rmp.crossy20.models.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        ImageView profileImage = convertView.findViewById(R.id.profile_image);

        Message message = getItem(position);

            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(message.getText());

        authorTextView.setText(message.getName());

        StorageReference picRef = storageReference.child(authorTextView.getText().toString());

        if(picRef.toString() != null) {
            final long ONE_MEGABYTE = 1024 * 1024;
            picRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImage.setImageBitmap(bmp);
                }
            });
        }

        return convertView;
    }
}
