package kore.ntnu.no.safespace.Data;

import java.util.List;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class Documentation extends Report{
    private final User submitter;

    public Documentation(Long ID, String description, List<Image> images, List<String> keywords, Project project, User submitter) {
        super(ID, description, images, keywords, project);
        this.submitter = submitter;
    }
}
