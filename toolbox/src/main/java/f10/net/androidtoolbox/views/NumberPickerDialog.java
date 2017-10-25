package f10.net.androidtoolbox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 21/03/2017.
 */
public class NumberPickerDialog extends Dialog implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    public interface OnNewValueListener{
        void onNewValueSelected(int newValue);
    }

    private int _value;
    private Button _cancelButton;
    private Button _okButton;
    private NumberPicker _picker;
    private OnNewValueListener _onNewValueListener;
    private int maxValue = 100;
    private int minValue = 0;

    public NumberPickerDialog(Context context, int initialValue) {
        super(context);
        _value = initialValue;
        init();
    }

    public void setOnNewValueListener(OnNewValueListener onNewValueListener) {
        _onNewValueListener = onNewValueListener;
    }

    private void init() {

        this.setContentView(R.layout.number_picker_dialog);

        _cancelButton = (Button) this.findViewById( R.id.buttonCancel);
        _okButton = (Button) this.findViewById(R.id.buttonSet);
        _picker = (NumberPicker) this.findViewById(R.id.numberPicker1);
        _picker.setMaxValue(maxValue);
        _picker.setMinValue(minValue);
        _picker.setValue(_value);
        _picker.setWrapSelectorWheel(false);

        _cancelButton.setOnClickListener(this);
        _okButton.setOnClickListener(this);
        _picker.setOnValueChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == _okButton && _onNewValueListener != null)
        {
            _onNewValueListener.onNewValueSelected(_value);
        }
        this.dismiss();
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        _value = newVal;
    }



    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

}
