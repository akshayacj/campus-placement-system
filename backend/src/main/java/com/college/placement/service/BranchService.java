package com.college.placement.service;

import com.college.placement.dto.BranchResponse;
import com.college.placement.mapper.BranchMapper;
import com.college.placement.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service encapsulating business operations and transactions on academic branches.
 * Uses constructor injection with final fields for immutability and testability.
 */
@Service
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(branchMapper::toResponse)
                .toList();
    }
}

