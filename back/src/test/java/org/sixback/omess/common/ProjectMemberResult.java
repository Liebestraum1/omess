package org.sixback.omess.common;

import org.sixback.omess.domain.member.model.entity.Member;
import org.sixback.omess.domain.project.model.entity.Project;
import org.sixback.omess.domain.project.model.entity.ProjectMember;

import java.util.List;

public class ProjectMemberResult {
    Member Member;
    List<Project> projects;
    List<ProjectMember> projectMembers;

    public ProjectMemberResult(Member member, List<Project> projects, List<ProjectMember> projectMembers) {
        Member = member;
        this.projects = projects;
        this.projectMembers = projectMembers;
    }

    public Member getMember() {
        return Member;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<ProjectMember> getProjectMembers() {
        return projectMembers;
    }
}
