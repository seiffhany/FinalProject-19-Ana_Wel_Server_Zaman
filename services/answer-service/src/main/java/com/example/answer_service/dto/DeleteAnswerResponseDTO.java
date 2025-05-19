package com.example.answer_service.dto;

import java.util.UUID;

public class DeleteAnswerResponseDTO {
    private int totalAnswersDeleted;
    private UUID deletedAnswerId;

    public DeleteAnswerResponseDTO(int totalAnswersDeleted, UUID deletedAnswerId) {
        this.totalAnswersDeleted = totalAnswersDeleted;
        this.deletedAnswerId = deletedAnswerId;
    }

    public int getTotalAnswersDeleted() {
        return totalAnswersDeleted;
    }

    public void setTotalAnswersDeleted(int totalAnswersDeleted) {
        this.totalAnswersDeleted = totalAnswersDeleted;
    }

    public UUID getDeletedAnswerId() {
        return deletedAnswerId;
    }

    public void setDeletedAnswerId(UUID deletedAnswerId) {
        this.deletedAnswerId = deletedAnswerId;
    }
}