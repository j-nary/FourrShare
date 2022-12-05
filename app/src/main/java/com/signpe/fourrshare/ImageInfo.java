package com.signpe.fourrshare;

import com.signpe.fourrshare.model.ImageDTO;

public class ImageInfo {
    ImageDTO imageDTO;
    String imguid;
    public ImageInfo(ImageDTO imageDTO,String imguid){
        this.imageDTO=imageDTO;
        this.imguid=imguid;
    }

    public ImageDTO getImageDTO() {
        return imageDTO;
    }

    public String getImguid() {
        return imguid;
    }

    public void setImageDTO(ImageDTO imageDTO) {
        this.imageDTO = imageDTO;
    }

    public void setImguid(String imguid) {
        this.imguid = imguid;
    }
}
