package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, User author) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem() != null ? ItemMapper.toItem(commentDto.getItem(), null) : null,
                author,
                commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem() != null ? ItemMapper.toItemDto(comment.getItem()) : null,
                comment.getAuthor() != null ? comment.getAuthor().getName() : null,
                comment.getCreated()
        );
    }

    public static List<CommentDto> toCommentsDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}