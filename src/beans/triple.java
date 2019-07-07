package beans;

public class triple {
    private int s_id, p_id, o_id;
    private String subject, predicate, object;

    public void setS(String s) {
        subject = s;
    }

    public String getS() {
        return subject;
    }

    public void setsid(int s) {
        s_id = s;
    }

    public int getsid() {
        return s_id;
    }

    public void setP(String p) {
        predicate = p;
    }

    public String getP() {
        return predicate;
    }

    public void setpid(int p) {
        p_id = p;
    }

    public int getpid() {
        return p_id;
    }

    public void setO(String o) {
        object = o;
    }

    public String getO() {
        return object;
    }

    public void setoid(int o) {
        o_id = o;
    }

    public int getoid() {
        return o_id;
    }
}
