package com.springdev.mepdev.JModels;

import com.cloudinary.StoredFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FVideo extends StoredFile {
    private MultipartFile video;
}
