package com.paygrid.auth.domain.model;

import com.paygrid.auth.domain.model.vo.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private String id;
    private RoleType name;
}
