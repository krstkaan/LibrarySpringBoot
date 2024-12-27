package com.sufaka.libraryspringboot.controller;

import com.sufaka.libraryspringboot.model.Book;
import com.sufaka.libraryspringboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Value("${server.port}")
    private String serverPort;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publishedDate") String publishedDate,
            @RequestParam("isbn") String isbn) {
        try {
            // Dosya Kontrolü
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dosya boş veya geçersiz.");
            }
            // Dizin Kontrolü ve Oluşturma
            String uploadDir = "bookimages";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.info("Dizin başarıyla oluşturuldu: " + directory.getAbsolutePath());
                } else {
                    logger.error("Dizin oluşturulurken hata oluştu: " + directory.getAbsolutePath());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dizin oluşturulurken hata oluştu.");
                }
            }
            // Dosya Yükleme İşlemi
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            logger.info("Dosya kaydedileceği yol: " + filePath.toString());
            file.transferTo(filePath.toFile());

            // Kitap nesnesi oluşturma ve kaydetme
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublishedDate(publishedDate);
            book.setIsbn(isbn);
            book.setPhotoUrl("http://localhost:" + serverPort + "/" + uploadDir + "/" + fileName);

            Book savedBook = bookRepository.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (IOException e) {
            logger.error("Dosya yükleme veya kaydetme hatası:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya yükleme veya kaydetme hatası: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Beklenmeyen bir hata oluştu:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

    // Tüm kitapları getir
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Belirli bir kitabı getir
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Belirli bir kitabı güncelle
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublishedDate(bookDetails.getPublishedDate());
            book.setIsbn(bookDetails.getIsbn());
            book.setPhotoUrl(bookDetails.getPhotoUrl());
            return bookRepository.save(book);
        }
        return null;
    }

    // Belirli bir kitabı sil
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}