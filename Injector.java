import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Injector {
    public static void inject(Object instance) {
        Intent intent;
        if (instance instanceof Activity) {
            Activity activity = (Activity) instance;
            intent = activity.getIntent();
        } else
            return;
        if (intent == null)
            return;
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectExtra.class)) {
                InjectExtra set = field.getAnnotation(InjectExtra.class);
                field.setAccessible(true); // should work on private fields
                try {
                    if (intent.hasExtra(set.value())) {
                        if (field.getType() == String.class)
                            field.set(instance, intent.getStringExtra(set.value()));
                        else if (field.getType() == int.class)
                            field.set(instance, intent.getIntExtra(set.value(), 0));
                        else if (field.getType() == boolean.class)
                            field.set(instance, intent.getBooleanExtra(set.value(), false));
                        else if (field.getType() == byte.class)
                            field.set(instance, intent.getByteExtra(set.value(), (byte) 0));
                        else if (field.getType() == short.class)
                            field.set(instance, intent.getShortExtra(set.value(), (short) 0));
                        else if (field.getType() == char.class)
                            field.set(instance, intent.getCharExtra(set.value(), '0'));
                        else if (field.getType() == long.class)
                            field.set(instance, intent.getLongExtra(set.value(), 0L));
                        else if (field.getType() == float.class)
                            field.set(instance, intent.getFloatExtra(set.value(), 0f));
                        else if (field.getType() == double.class)
                            field.set(instance, intent.getDoubleExtra(set.value(), 0d));
                        else if (field.getType() == Serializable.class)
                            field.set(instance, intent.getSerializableExtra(set.value()));
                        else if (field.getType() == Parcelable.class)
                            field.set(instance, intent.getParcelableExtra(set.value()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
