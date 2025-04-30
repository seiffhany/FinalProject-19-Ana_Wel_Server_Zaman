package com.example.answer_service.commands.command;


import java.util.UUID;

public interface Command {
    void execute(UUID answerId);
    void undo(UUID answerId);
}
