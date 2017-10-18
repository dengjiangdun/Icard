package util.cmcc.johndon.com.icard1;

/**
 * Created by DELL on 2017/10/14.
 */

public class IDResult {
    private String error_code;
    private String reason;
    private Result result;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
