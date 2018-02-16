package ke.merlin.utils;

/**
 * Created by mecmurimi on 22/03/2017.
 */

public class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String string, Object tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
}
