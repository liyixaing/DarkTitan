package lanjing.com.titan.response;

/**
 * Created by chenxi on 2019/5/14.
 */

public class ListWalletImportResponse {

    /**
     * msg : ok
     * address : fafagagsgfnfklvdsfsmg
     * code : 200
     * userName : hello
     * keyes : 99999928
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTc4NTQ0MjI0OTAsInBheWxvYWQiOiJcIjk5OTk5OTI4XCIifQ.6c-zAi2kYuOYqh2AQOPa0zxfI6qsxR_o4S3tdTs14ns
     */

    private String msg;
    private int code;
    public Data data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String msg;
        private String address;
        private String code;
        private String phone;
        private String inviter;
        private String userName;
        private String keyes;
        private String token;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getInviter() {
            return inviter;
        }

        public void setInviter(String inviter) {
            this.inviter = inviter;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getKeyes() {
            return keyes;
        }

        public void setKeyes(String keyes) {
            this.keyes = keyes;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
