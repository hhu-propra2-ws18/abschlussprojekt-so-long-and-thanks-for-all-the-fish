package de.hhu.ProPra.conflict.model;

import lombok.Data;

@Data
public class Conflict {

    private long userId;
    private long adminId;
    private long lendingId;

}
