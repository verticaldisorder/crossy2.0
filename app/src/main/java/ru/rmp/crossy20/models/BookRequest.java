package ru.rmp.crossy20.models;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import ru.rmp.crossy20.activities.ApplicationActivity;
import ru.rmp.crossy20.utils.ApplicationDialogFragment;

public class BookRequest extends Thread {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    String applicant;
    String bookholder;
    String book;
    Context context;
    ArrayList<BookRequest> requests = new ArrayList<>();

    public BookRequest() {}

    public BookRequest(String applicant, String bookholder, String book, Context context) {
        this.applicant = applicant;
        this.bookholder = bookholder;
        this.book = book;
        this.context = context;

        //TODO обработка заявки
        requests.add(this);
    }

    @Override
    public void run() {
        System.out.println("Тред обработки заявок начался");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (BookRequest bq : requests) {
                if (bq.getBookholder().equals(user.getUid())) {
                    //TODO юзер получает заявку ALERT DIALOG
                    ApplicationActivity activity = new ApplicationActivity();
                    Intent intent = new Intent(activity.getBaseContext(), ApplicationActivity.class);
                    intent.putExtra("Applicant", applicant);
                    intent.putExtra("Book", book);

                    context.startActivity(intent);

                }
            }

        }
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getBookholder() {
        return bookholder;
    }

    public void setBookholder(String bookholder) {
        this.bookholder = bookholder;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "applicant='" + applicant + '\'' +
                ", bookholder='" + bookholder + '\'' +
                ", book='" + book + '\'' +
                '}';
    }
}
