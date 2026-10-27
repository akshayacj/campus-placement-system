package com.college.placement.mapper;

import com.college.placement.dto.BranchResponse;
import com.college.placement.entity.Branch;
import org.springframework.stereotype.Component;

/**
 * Explicit mapper between Branch entity and BranchResponse DTO.
 * Avoids bytecode manipulation or hidden reflection to keep logic transparent for teaching.
 */
@Component
public class BranchMapper {

    public BranchResponse toResponse(Branch branch) {
        if (branch == null) {
            return null;
        }
        return new BranchResponse(branch.getId(), branch.getCode(), branch.getName());
    }
}

