package com.secure.secure_back_end.services.interfaces;

import com.secure.secure_back_end.dto.comment.binding.CommentCreateForm;
import com.secure.secure_back_end.dto.comment.binding.CommentEditForm;
import com.secure.secure_back_end.dto.comment.view.CommentViewModel;

import java.util.List;

public interface CommentService
{
    void insertComment(CommentCreateForm form, Long ticketId);

    List<CommentViewModel> getTicketComments(long ticketId);

    List<CommentViewModel> getUserComments(Long userId);

    void editComment(CommentEditForm form, Long commentId);

    void deleteComment(Long commentId);
}