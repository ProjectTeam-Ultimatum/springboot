//package ultimatum.project.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ultimatum.project.dto.reviewDTO.DeleteImageResponse;
//import ultimatum.project.dto.reviewDTO.ReviewImageResponse;
//import ultimatum.project.service.review.S3Service;
//
//@Log4j2
//@Tag(name = "reviewsImages", description = "사용자 게시글 이미지")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/reviews")
//@CrossOrigin(origins = "*")
//public class ReviewImageController {
//
//    private final S3Service s3Service;
//
//    @DeleteMapping(consumes = {"multipart/form-data"})
//    @Operation(summary = "이미지 삭제")
//    public ResponseEntity<DeleteImageResponse> imageUpload(@RequestParam("imageId") Long reviewImageId){
//
//    }
//}
