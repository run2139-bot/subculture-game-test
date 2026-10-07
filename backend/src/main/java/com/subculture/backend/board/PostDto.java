package com.subculture.backend.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public final class PostDto {

    private PostDto() {
    }

    public record CreateRequest(
            @NotBlank @Size(max = 100) String title,
            @NotBlank @Size(max = 5000) String content,
            @NotBlank @Size(max = 30) String author,
            @NotBlank @Size(min = 4, max = 50) String password) {
    }

    public record UpdateRequest(
            @NotBlank @Size(max = 100) String title,
            @NotBlank @Size(max = 5000) String content,
            @NotBlank String password) {
    }

    public record Summary(Long id, String title, String author, LocalDateTime createdAt) {
        static Summary from(Post p) {
            return new Summary(p.getId(), p.getTitle(), p.getAuthor(), p.getCreatedAt());
        }
    }

    public record Detail(Long id, String title, String content, String author,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        static Detail from(Post p) {
            return new Detail(p.getId(), p.getTitle(), p.getContent(), p.getAuthor(),
                    p.getCreatedAt(), p.getUpdatedAt());
        }
    }

    public record PageResponse(List<Summary> items, int page, int size, int totalPages, long totalElements) {
    }
}
