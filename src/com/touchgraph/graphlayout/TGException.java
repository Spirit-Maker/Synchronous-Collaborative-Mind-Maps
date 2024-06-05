package com.touchgraph.graphlayout;

public class TGException extends Exception {

    public final static int NODE_DOESNT_EXIST = 2;

    public static final int NODE_EXISTS = 1;

    public final static int NODE_NO_ID = 3;

    protected int id = -1;

    public Exception exception = null;

    public TGException(int id) {
        super();
        this.id = id;
    }

    public TGException(int id, String message) {
        super(message);
        this.id = id;
    }

    public TGException(String message) {
        super(message);
    }

    public TGException(Exception exception) {
        super();
        this.exception = exception;
    }

    public int getId() {
        return id;
    }

}
