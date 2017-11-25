package kore.ntnu.no.safespace.dto;

import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.data.User;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class DocumentDTO extends ReportDTO {
    private User user;
    public DocumentDTO(Long id, String title, String description, Project project, User submitter) {
        super(id, title, description, project);
        this.user = submitter;
    }

    public User getUser() {
        return user;
    }
}
