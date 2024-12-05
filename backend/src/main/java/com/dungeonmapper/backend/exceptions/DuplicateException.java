package com.dungeonmapper.backend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException extends Exception {

    private static final long serialVersionUID = -1819210920192L;
    private String errMsg = "Risorsa già presente";

    public DuplicateException() {
        super();
    }

    public DuplicateException(String msg) {
        super(msg);
        this.errMsg = msg;
    }
}