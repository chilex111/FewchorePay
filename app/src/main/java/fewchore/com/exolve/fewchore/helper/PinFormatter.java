package fewchore.com.exolve.fewchore.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import fewchore.com.exolve.fewchore.listener.PinTextListener;


/*
 * Created by Chidinma Ekenne on 17/01/2018..
 */
public class PinFormatter implements TextWatcher {

    private EditText nextEditText;
    private PinTextListener listener;

    public PinFormatter(EditText nextEditText, PinTextListener listener) {
        this.nextEditText = nextEditText;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1){
            if (nextEditText != null){
                nextEditText.requestFocus();

                if (listener != null){
                    listener.onTextChanged();
                }
            }
        }
    }
}
