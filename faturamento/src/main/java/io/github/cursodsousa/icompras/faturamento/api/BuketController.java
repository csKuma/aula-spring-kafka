package io.github.cursodsousa.icompras.faturamento.api;

import io.github.cursodsousa.icompras.faturamento.bucket.BucketFile;
import io.github.cursodsousa.icompras.faturamento.bucket.BucketService;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BuketController {
    private final BucketService bucketService;

    @PostMapping
    private ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            MediaType type = MediaType.parseMediaType(file.getContentType());
            var bucketFile = new BucketFile(file.getOriginalFilename(), is, type, file.getSize());
            bucketService.upload(bucketFile);
            return ResponseEntity.ok().body("arquivo Enviado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("erro ao enviar arquivo " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getUrl(@RequestParam("fileName")String fileName) {
        try {
            String url = bucketService.getUrl(fileName);
            return ResponseEntity.ok().body(url);
        }catch (Exception e){
            return ResponseEntity.status(500).body("erro ao obter url do arquivo " + e.getMessage());
        }
    }
}
