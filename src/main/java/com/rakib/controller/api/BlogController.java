package com.rakib.controller.api;

import com.google.common.collect.ImmutableMap;
import com.rakib.domain.Blog;
import com.rakib.domain.enums.DataType;
import com.rakib.service.BlogService;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.BlogDetailsDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("blog")
    //@PreAuthorize("hasAuthority('BLOGGER')")
    public ResponseEntity<?> saveBlog(@Valid @RequestBody BlogDTO blogDTO) throws NotFoundException {
        Blog blog = blogService.saveBlog(blogDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }

    @GetMapping("blog")
    //@PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<?> getBlog(@RequestParam(required = false) DataType action, Pageable pageable) {
        Page<BlogDetailsDTO> blog = blogService.getBlog(action, pageable);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }

    @GetMapping("blog/{id}")
    //@PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<?> getBlog(@PathVariable Long id) {
        BlogDetailsDTO blog = blogService.getBlogById(id);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }
    @GetMapping("blog/user/{id}")
    //@PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<?> getBlogByUser(@PathVariable Long id, Pageable pageable) {
        Page<BlogDetailsDTO> blog = blogService.getBlogByUserId(id, pageable);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }

    @PutMapping("blog/{id}")
    //@PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO) {
        Blog blog = blogService.updateBlog(id, blogDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }

    @DeleteMapping("blog/{id}")
    //@PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) throws NotFoundException {
        String blog = blogService.deleteBlog(id);
        return ResponseEntity.ok().body(ImmutableMap.of("data", blog));
    }
}
