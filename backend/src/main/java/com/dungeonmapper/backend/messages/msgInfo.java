package com.dungeonmapper.backend.messages;

import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class msgInfo {
    public LocalDate date;
    public String message;
}