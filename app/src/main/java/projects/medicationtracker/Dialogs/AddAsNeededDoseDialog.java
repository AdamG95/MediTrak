package projects.medicationtracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import projects.medicationtracker.Fragments.TimePickerFragment;
import projects.medicationtracker.Helpers.DBHelper;
import projects.medicationtracker.R;
import projects.medicationtracker.SimpleClasses.Medication;

public class AddAsNeededDoseDialog extends DialogFragment
{
    private final ArrayList<Medication> medications;
    private final DBHelper db;
    private AutoCompleteTextView medicationNames;
    private TextInputEditText timeTaken;
    private AutoCompleteTextView yourMeds;
    private LocalDate date;

    public AddAsNeededDoseDialog(ArrayList<Medication> medications, LocalDate dateTaken, DBHelper database)
    {
        db = database;
        this.medications = medications;
        date = dateTaken;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances)
    {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_as_needed, null));
        builder.setTitle(R.string.add_dose);

        builder.setPositiveButton(R.string.save, ((dialogInterface, i) -> save()));
        builder.setNegativeButton(R.string.close, ((dialogInterface, i) -> dismiss()));

        dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        yourMeds = dialog.findViewById(R.id.medicationNames);
        timeTaken = dialog.findViewById(R.id.doseTimeTaken);

        yourMeds.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!timeTaken.getText().toString().isEmpty())
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
            }
        });

        timeTaken.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!yourMeds.getText().toString().isEmpty())
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
            }
        });

        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ArrayList<String> medNames = new ArrayList<>();
        ArrayAdapter<String> medNamesAdapter;

        medications.forEach(m -> medNames.add(m.getName()));

        medNamesAdapter = new ArrayAdapter<>(
                getDialog().getContext(), android.R.layout.simple_dropdown_item_1line, medNames
        );

        yourMeds.setAdapter(medNamesAdapter);

        timeTaken.setShowSoftInputOnFocus(false);

        timeTaken.setOnFocusChangeListener((view, b) ->
        {
            if (b)
            {
                DialogFragment dialogFragment = new TimePickerFragment(timeTaken);
                dialogFragment.show(getParentFragmentManager(), null);
            }
        });
    }

    private void save()
    {
        String selectedMedName = yourMeds.getText().toString();

        Medication med = medications.stream().filter(
                m -> Objects.equals(m.getName(), selectedMedName)
        ).collect(Collectors.toCollection(ArrayList::new)).get(0);
    }
}
