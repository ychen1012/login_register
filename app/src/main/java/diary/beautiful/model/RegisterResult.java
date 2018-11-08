package diary.beautiful.model;

public class RegisterResult {
    private String result;
    private String msg;

    public RegisterResult() {

    }

    public RegisterResult(String result, String msg) {
        this.msg = msg;
        this.result = result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

}
