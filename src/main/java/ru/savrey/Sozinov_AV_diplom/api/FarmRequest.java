package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;

@Data
public class FarmRequest {
    private final long userId;
    private String title;
    private String address;
}
