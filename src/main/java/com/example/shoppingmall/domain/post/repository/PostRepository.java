package com.example.shoppingmall.domain.post.repository;

import com.example.shoppingmall.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    @Query(value = "select * from post where id < :id order by id desc limit :size", nativeQuery = true)
    List<Post> findAllPostsByCursorHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from post order by id desc limit :size", nativeQuery = true)
    List<Post> findAllPostsByCursorNoKey(@Param("size") int size);
}
