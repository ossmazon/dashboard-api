package com.mazon.dashboard_api.service;

import com.mazon.dashboard_api.dto.PageResponseDTO;
import com.mazon.dashboard_api.dto.UserDTO;
import com.mazon.dashboard_api.model.User;
import com.mazon.dashboard_api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).
                map(UserDTO::new);
    }

    public UserDTO createUser(User user) {
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<UserDTO> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setWebsite(updatedUser.getWebsite());
                    existingUser.setAddress(updatedUser.getAddress());
                    existingUser.setCompany(updatedUser.getCompany());

                    User savedUser = userRepository.save(existingUser);

                    return new UserDTO(savedUser);
                });
    }

    public PageResponseDTO<UserDTO> getUsersPaginatedList(int page, int size, String sortBy, String direction) {

        page = Math.max(page, 0);

        size = Math.min(size, 100);

        size = Math.max(size, 1);


        Sort sort;
        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();

        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDTO> content = userPage.getContent()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isFirst(),
                userPage.isLast()
        );
    }
}
