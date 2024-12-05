package com.dungeonmapper.backend.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends Exception {

    private static final long serialVersionUID = -2819210920192L;
    private String errMsg = "Resource not found";

    public NotFoundException(){
        super();
    }

    public NotFoundException(String msg) {
        super(msg);
        this.errMsg = msg;
    }
}
