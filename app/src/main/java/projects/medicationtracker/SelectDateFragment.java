package projects.medicationtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd)
    {
        populateSetDate(yy, mm+1, dd);
    }

    public void populateSetDate(int year, int month, int day)
    {
        TextView textView = getActivity().findViewById(R.id.startDate);
        String date = month + "/" + day + "/" + year;
        textView.setText(date);
    }

}