package ke.merlin.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by mecmurimi on 17/07/2017.
 */

public class MyDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) {
        try {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date read(JsonReader in) {
        try {
            if (in != null) {
                return new Date(in.nextLong());
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
