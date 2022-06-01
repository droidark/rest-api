package xyz.krakenkat.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.krakenkat.restapi.domain.model.Post;
import xyz.krakenkat.restapi.domain.model.User;
import xyz.krakenkat.restapi.domain.model.UserNotFoundException;
import xyz.krakenkat.restapi.domain.repository.PostRepository;
import xyz.krakenkat.restapi.domain.repository.UserRepository;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @GetMapping
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-" + id);
        // all-users SERVICE_PATH + /users
        // retrieve all users

        EntityModel<User> model = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        // HATEOAS
        return model;
    }

    // ResponseEntity<T> to return the correct code and status
    // More information about ResponseEntity:
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/RequestEntity.html
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        User savedUSer = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUSer.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // POST OPERATIONS
    // Retrieve all posts by user id
    @GetMapping(path = "/{id}/posts")
    public ResponseEntity<?> retrievePostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-" + id);
        return new ResponseEntity<>(userRepository.findById(id).get().getPosts(), HttpStatus.OK);
    }

    // Create a post for a specific user
    @PostMapping(path = "/{id}/posts")
    public ResponseEntity<?> createPostByUser(@PathVariable int id, @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-" + id);
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
