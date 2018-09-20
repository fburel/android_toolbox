package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import f10.net.androidtoolbox.Logger.Log;
import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 17/05/2017.
 */

public class TextViewCell extends Cell<String> {

    private final String _hint;
    private String _value;
    private WeakReference<EditText> _editTextRef;

    public TextViewCell(int tag, FormFragment form, String hint, String text)
    {
        super(tag, form);
        _value = text;
        _hint = hint;
    }

    @Override
    protected View getView(Context context) {

        View cell = LayoutInflater.from(context).inflate(R.layout.forms_cell_input, null);

        EditText editText = (EditText) cell.findViewById(R.id.editText);
        editText.setText(_value);
        editText.setHint(_hint);
        editText.addTextChangedListener(new TextChangedListener<EditText>(editText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                Log.d("focus", "onTextChanged : " + s);
                _value = target.getText().toString();
                notifyChanged(_value);
            }
        });
        _editTextRef = new WeakReference<EditText>(editText);

        return applyBackgroundColor(cell);
    }

    public void setValue(String value) {
        _value = value;
        _editTextRef.get().setText(_value);
    }

    public abstract class TextChangedListener<T> implements TextWatcher {
        private T target;

        public TextChangedListener(T target) {
            this.target = target;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public abstract void onTextChanged(T target, Editable s);
    }

    @Override
    protected void onGainingFocus(Context c) {
        super.onGainingFocus(c);

    }

    @Override
    protected void onLoosingFocus(Context c) {
        super.onLoosingFocus(c);

        Log.d("TextView", "loosing focus");

        _editTextRef.get().setFocusableInTouchMode(false);
        _editTextRef.get().setFocusable(false);
        _editTextRef.get().setFocusableInTouchMode(true);
        _editTextRef.get().setFocusable(true);
    }

    public static class Builder {
        int _tag;
        FormFragment _fragment;
        private String _hint = "";
        private String _text = "";

        public Builder(int tag, FormFragment formFragment) {
            _tag = tag; _fragment = formFragment;
        }

        public TextViewCell.Builder setHint(String hint) {
            _hint = hint;
            return this;
        }

        public TextViewCell.Builder setText(String text) {
            _text = text;
            return this;
        }


        public Cell build() {
            return new TextViewCell(_tag, _fragment, _hint, _text);
        }
    }
}
