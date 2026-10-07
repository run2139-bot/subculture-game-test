package com.subculture.backend.board;

import com.subculture.backend.board.PostDto.CreateRequest;
import com.subculture.backend.board.PostDto.Detail;
import com.subculture.backend.board.PostDto.PageResponse;
import com.subculture.backend.board.PostDto.Summary;
import com.subculture.backend.board.PostDto.UpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PostService {

    private static final int MAX_PAGE_SIZE = 50;

    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse list(int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), MAX_PAGE_SIZE),
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> result = postRepository.findAll(pageable);
        return new PageResponse(result.map(Summary::from).getContent(), result.getNumber(), result.getSize(),
                result.getTotalPages(), result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Detail get(Long id) {
        return Detail.from(find(id));
    }

    public Detail create(CreateRequest req) {
        Post post = new Post(req.title().trim(), req.content().trim(), req.author().trim(),
                passwordEncoder.encode(req.password()));
        return Detail.from(postRepository.save(post));
    }

    public Detail update(Long id, UpdateRequest req) {
        Post post = find(id);
        checkPassword(post, req.password());
        post.update(req.title().trim(), req.content().trim());
        return Detail.from(postRepository.saveAndFlush(post));
    }

    public void delete(Long id, String password) {
        Post post = find(id);
        checkPassword(post, password);
        postRepository.delete(post);
    }

    private Post find(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

    private void checkPassword(Post post, String rawPassword) {
        if (rawPassword == null || !passwordEncoder.matches(rawPassword, post.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }
    }
}
