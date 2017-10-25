package f10.net.androidtoolbox.navigation;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fl0 on 05/07/2017.
 */

public class BundleBuilder {

    private Bundle _bundle;


    public BundleBuilder()
    {
        _bundle = new Bundle();
    }

    public static BundleBuilder withAll(Bundle bundle) {
        return new BundleBuilder().putAll(bundle);
    }

    public static BundleBuilder withByte(String key, byte value) {
        return new BundleBuilder().putByte(key, value);

    }

    public static BundleBuilder withChar(String key, char value) {
        return new BundleBuilder().putChar(key, value);
    }

    public static BundleBuilder withShort(String key, short value) {
        return new BundleBuilder().putShort(key, value);
    }

    public static BundleBuilder withFloat(String key, float value) {
        return new BundleBuilder().putFloat(key, value);
    }

    public static BundleBuilder withCharSequence(String key, CharSequence value) {
        return new BundleBuilder().putCharSequence(key, value);
    }

    public static BundleBuilder withParcelable(String key, Parcelable value) {
        return new BundleBuilder().putParcelable(key, value);
    }

    public static BundleBuilder withSize(String key, Size value) {
        return new BundleBuilder().putSize(key, value);
    }

    public static BundleBuilder withSizeF(String key, SizeF value) {
        return new BundleBuilder().putSizeF(key, value);
    }

    public static BundleBuilder withParcelableArray(String key, Parcelable[] value) {
        return new BundleBuilder().putParcelableArray(key, value);
    }

    public static BundleBuilder withParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        return new BundleBuilder().putParcelableArrayList(key, value);
    }

    public static BundleBuilder withSparseParcelableArray(String key, SparseArray<? extends Parcelable> value) {
        return new BundleBuilder().putSparseParcelableArray(key, value);
    }

    public static BundleBuilder withIntegerArrayList(String key, ArrayList<Integer> value) {
        return new BundleBuilder().putIntegerArrayList(key, value);
    }

    public static BundleBuilder withStringArrayList(String key, ArrayList<String> value) {
        return new BundleBuilder().putStringArrayList(key, value);
    }

    public static BundleBuilder withCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        return new BundleBuilder().putCharSequenceArrayList(key, value);
    }

    public static BundleBuilder withSerializable(String key, Serializable value) {
        return new BundleBuilder().putSerializable(key, value);
    }

    public static BundleBuilder withByteArray(String key, byte[] value) {
        return new BundleBuilder().putByteArray(key, value);
    }

    public static BundleBuilder withShortArray(String key, short[] value) {
        return new BundleBuilder().putShortArray(key, value);
    }

    public static BundleBuilder withCharArray(String key, char[] value) {
        return new BundleBuilder().putCharArray(key, value);
    }

    public static BundleBuilder withFloatArray(String key, float[] value) {
        return new BundleBuilder().putFloatArray(key, value);
    }

    public static BundleBuilder withCharSequenceArray(String key, CharSequence[] value) {
        return new BundleBuilder().putCharSequenceArray(key, value);
    }

    public BundleBuilder withBundle(String key, Bundle value) {
        return new BundleBuilder().putBundle(key, value);
    }

    public static BundleBuilder withBinder(String key, IBinder value) {
        return new BundleBuilder().putBinder(key, value);
    }

    public static BundleBuilder withBoolean(String key, boolean value) {
        return new BundleBuilder().putBoolean(key, value);
    }

    public static BundleBuilder withInt(String key, int value) {
        return new BundleBuilder().putInt(key, value);
    }

    public static BundleBuilder withLong(String key, long value) {
        return new BundleBuilder().putLong(key, value);
    }

    public static BundleBuilder withDouble(String key, double value) {
        return new BundleBuilder().putDouble(key, value);
    }

    public static BundleBuilder withString(String key, String value) {
        return new BundleBuilder().putString(key, value);
    }

    public static BundleBuilder withBooleanArray(String key, boolean[] value) {
        return new BundleBuilder().putBooleanArray(key, value);
    }

    public static BundleBuilder vIntArray(String key, int[] value) {
        return new BundleBuilder().putIntArray(key, value);
    }

    public static BundleBuilder withLongArray(String key, long[] value) {
        return new BundleBuilder().putLongArray(key, value);
    }

    public static BundleBuilder withDoubleArray(String key, double[] value) {
        return new BundleBuilder().putDoubleArray(key, value);
    }

    public static BundleBuilder withStringArray(String key, String[] value) {
        return new BundleBuilder().putStringArray(key, value);
    }







    public BundleBuilder putAll(Bundle bundle) {
        _bundle.putAll(bundle);
        return this;
    }

    public BundleBuilder putByte(String key, byte value) {
        _bundle.putByte(key, value);
        return this;
    }

    public BundleBuilder putChar(String key, char value) {
        _bundle.putChar(key, value);
        return this;
    }

    public BundleBuilder putShort(String key, short value) {
        _bundle.putShort(key, value);
        return this;
    }

    public BundleBuilder putFloat(String key, float value) {
        _bundle.putFloat(key, value);
        return this;
    }

    public BundleBuilder putCharSequence(String key, CharSequence value) {
        _bundle.putCharSequence(key, value);
        return this;
    }

    public BundleBuilder putParcelable(String key, Parcelable value) {
        _bundle.putParcelable(key, value);
        return this;
    }

    public BundleBuilder putSize(String key, Size value) {
        _bundle.putSize(key, value);
        return this;
    }

    public BundleBuilder putSizeF(String key, SizeF value) {
        _bundle.putSizeF(key, value);
        return this;
    }

    public BundleBuilder putParcelableArray(String key, Parcelable[] value) {
        _bundle.putParcelableArray(key, value);
        return this;
    }

    public BundleBuilder putParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        _bundle.putParcelableArrayList(key, value);
        return this;
    }

    public BundleBuilder putSparseParcelableArray(String key, SparseArray<? extends Parcelable> value) {
        _bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public BundleBuilder putIntegerArrayList(String key, ArrayList<Integer> value) {
        _bundle.putIntegerArrayList(key, value);
        return this;
    }

    public BundleBuilder putStringArrayList(String key, ArrayList<String> value) {
        _bundle.putStringArrayList(key, value);
        return this;
    }

    public BundleBuilder putCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        _bundle.putCharSequenceArrayList(key, value);
        return this;    }

    public BundleBuilder putSerializable(String key, Serializable value) {
        _bundle.putSerializable(key, value);
        return this;    }

    public BundleBuilder putByteArray(String key, byte[] value) {
        _bundle.putByteArray(key, value);
        return this;
    }

    public BundleBuilder putShortArray(String key, short[] value) {
        _bundle.putShortArray(key, value);
        return this;
    }

    public BundleBuilder putCharArray(String key, char[] value) {
        _bundle.putCharArray(key, value);
        return this;
    }

    public BundleBuilder putFloatArray(String key, float[] value) {
        _bundle.putFloatArray(key, value);
        return this;
    }

    public BundleBuilder putCharSequenceArray(String key, CharSequence[] value) {
        _bundle.putCharSequenceArray(key, value);
        return this;
    }

    public BundleBuilder putBundle(String key, Bundle value) {
        _bundle.putBundle(key, value);
        return this;
    }

    public BundleBuilder putBinder(String key, IBinder value) {
        _bundle.putBinder(key, value);
        return this;
    }

    public BundleBuilder putBoolean(String key, boolean value) {
        _bundle.putBoolean(key, value);
        return this;
    }

    public BundleBuilder putInt(String key, int value) {
        _bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder putLong(String key, long value) {
        _bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder putDouble(String key, double value) {
        _bundle.putDouble(key, value);
        return this;
    }

    public BundleBuilder putString(String key, String value) {
        _bundle.putString(key, value);
        return this;
    }

    public BundleBuilder putBooleanArray(String key, boolean[] value) {
        _bundle.putBooleanArray(key, value);
        return this;
    }

    public BundleBuilder putIntArray(String key, int[] value) {
        _bundle.putIntArray(key, value);
        return this;
    }

    public BundleBuilder putLongArray(String key, long[] value) {
        _bundle.putLongArray(key, value);
        return this;
    }

    public BundleBuilder putDoubleArray(String key, double[] value) {
        _bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleBuilder putStringArray(String key, String[] value) {
        _bundle.putStringArray(key, value);
        return this;
    }

    public Bundle createBundle(){
        return _bundle;
    }
}
