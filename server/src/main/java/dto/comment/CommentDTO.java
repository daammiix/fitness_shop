package dto.comment;

import model.facility.Comment;

public class CommentDTO {
    public String id;
    public String username;
    public String firstname;
    public String lastname;
    public String content;
    public boolean approved;

    public CommentDTO(){}

    public CommentDTO(Comment comment){
        this.id = comment.id;
        this.username = comment.user_id;
        this.content = comment.content;
        this.approved = comment.approved;
    }
}
