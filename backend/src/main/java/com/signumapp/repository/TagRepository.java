package com.signumapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signumapp.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);
}
