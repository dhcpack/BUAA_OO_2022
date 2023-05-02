public class MyConn {
    private final int id1;
    private final int id2;
    private final int idLo;
    private final int idHi;

    public MyConn(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        // ensure idLo <= idHi
        if (id1 <= id2) {
            this.idLo = id1;
            this.idHi = id2;
        } else {
            this.idLo = id2;
            this.idHi = id1;
        }
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public int getIdLo() {
        return idLo;
    }

    public int getIdHi() {
        return idHi;
    }

    public int hashCode() {
        return idLo * idHi;
    }

    public boolean equals(Object obj) {
        // of same class
        if (obj == null || !(obj instanceof MyConn)) {
            return false;
        }
        // lo, hi equals
        MyConn rhs = (MyConn) obj;
        return rhs.idLo == idLo && rhs.idHi == idHi;
    }
}
