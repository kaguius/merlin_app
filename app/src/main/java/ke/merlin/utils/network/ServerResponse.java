package ke.merlin.utils.network;

/**
 * Created by mecmurimi on 23/07/2017.
 */

public class ServerResponse {
    Boolean success = false;
    String message;

    public ServerResponse(boolean b, String s) {
        success = b;
        message = s;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
