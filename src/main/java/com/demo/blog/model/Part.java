package com.demo.blog.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Part {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String code;
}