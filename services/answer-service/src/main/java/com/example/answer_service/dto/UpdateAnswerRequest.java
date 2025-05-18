package com.example.answer_service.dto;

public class UpdateAnswerRequest {
    private String content;
    private byte[] image;
    private boolean removeImage;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] imageInByte) {
        this.image = image;
    }
    public boolean isRemoveImage() {
        return removeImage;
    }

    public void setRemoveImage(boolean removeImage) {
        this.removeImage = removeImage;
    }
}
