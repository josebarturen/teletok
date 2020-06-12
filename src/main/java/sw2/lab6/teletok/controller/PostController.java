package sw2.lab6.teletok.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.entity.PostLike;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostLikeRepository;
import sw2.lab6.teletok.repository.PostRepository;
import sw2.lab6.teletok.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PostController {

    private static String UPLOADED_FOLDER = "/tmp/teletok/";

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(Model model, @RequestParam(value = "query", required = false) String query, HttpSession session){
        if(query == null){
            session.setAttribute("query", null);
            model.addAttribute("postList", postRepository.findByOrderByCreationDateDesc());
        } else {
            session.setAttribute("query", query);
            model.addAttribute("postList", postRepository.findByDescriptionContainingOrUserUsernameContainingOrderByCreationDateDesc(query, query));
        }
        return "post/list";
    }

    @GetMapping("/post/new")
    public String newPost(@ModelAttribute("post") Post post){
        return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost(@RequestParam("postFile") MultipartFile file, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult, RedirectAttributes attr, HttpSession session) {
        if(bindingResult.hasErrors()){
            return "post/new";
        } else {
            if (file.isEmpty()) {
                attr.addFlashAttribute("msg", "Por favor suba un archivo.");
                return "redirect:/post/new";
            }
            try {
                String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
                Files.write(Paths.get(UPLOADED_FOLDER + fileName), file.getBytes());
                post.setCreationDate(new Date());
                post.setMediaUrl(fileName);
                post.setUser((User) session.getAttribute("user"));
                postRepository.save(post);
                attr.addFlashAttribute("msg","La publicación ha subido correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/";
        }
    }

    @GetMapping(value = "/post/file/{file_name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("file_name") String fileName) throws IOException {
        File file = new File(UPLOADED_FOLDER + fileName);
        HttpHeaders respHeaders = new HttpHeaders();
        return new ResponseEntity<FileSystemResource>(
                new FileSystemResource(file), respHeaders, HttpStatus.OK
        );
    }

    @GetMapping("/post/{id}")
    public String viewPost(@ModelAttribute("postComment") PostComment postComment, Model model, @PathVariable("id") int id, HttpSession session) {
        return loadPost(id,model,session);
    }

    @PostMapping("/post/comment")
    public String postComment(@RequestParam("postId") int postId, @ModelAttribute("postComment") @Valid PostComment postComment, BindingResult bindingResult, Model model, RedirectAttributes attr, HttpSession session) {
        if(bindingResult.hasErrors()){
            return loadPost(postId,model,session);
        } else {
            Post post = new Post();
            post.setId(postId);
            postComment.setPost(post);
            postComment.setUser((User) session.getAttribute("user"));
            postCommentRepository.save(postComment);
            attr.addFlashAttribute("msg","Comentario Registrado Exitosamente.");
            return "redirect:/post/" + postId;
        }
    }

    @PostMapping("/post/like")
    public String postLike(@RequestParam("postId") int postId, Model model, RedirectAttributes attr, HttpSession session) {
        Post post = new Post();
        post.setId(postId);
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser((User) session.getAttribute("user"));
        postLikeRepository.save(postLike);
        attr.addFlashAttribute("msg","Like Registrado Exitosamente.");
        return "redirect:/post/" + postId;
    }

    private String loadPost(int id, Model model, HttpSession session){
        Optional<Post> optPost = postRepository.findById(id);
        if (optPost.isPresent()) {
            // Check if Current User likes post
            Boolean userLikePost = true;
            if(session.getAttribute("user") != null) {
                userLikePost = false;
                User user = (User) session.getAttribute("user");
                for (PostLike pl : optPost.get().getLikes()) {
                    if (pl.getUser().getId() == user.getId()){
                        userLikePost = true;
                    }
                }
            }
            model.addAttribute("userLikePost", userLikePost);
            model.addAttribute("post", optPost.get());
            return "post/view";
        }
        return "redirect:/";
    }
}
