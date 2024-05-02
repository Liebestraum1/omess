package org.sixback.omess.common.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.project.dto.response.GetProjectMemberResponse;
import org.sixback.omess.domain.project.service.ProjectService;
import org.springframework.http.server.PathContainer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectPermissionFilter implements Filter {
    private final ProjectService projectService;
    private final PathPattern pathPattern = new PathPatternParser().parse("/api/v1/projects/{projectId}/**");

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
    ) throws IOException, ServletException {
        log.debug("filter start");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        if (isMatches(requestURI)) {
            log.debug("uri matched");
            HttpSession session = httpServletRequest.getSession();
            Long memberId = (Long) session.getAttribute("memberId");
            log.debug("memberId ={}", memberId);
            
            Long projectId = getProjectId(requestURI);
            List<GetProjectMemberResponse> memberProjects = projectService.getProjects(memberId).projects();
            log.debug("memberProjects: {}", memberProjects);
            if (isNotProjectMember(projectId, memberProjects)) {
                throw new AccessDeniedException("프로젝트의 멤버가 아닙니다!");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
        log.debug("filter end");
    }
    
    private boolean isMatches(String uri) {
        return pathPattern.matches(PathContainer.parsePath(uri));
    }

    private boolean isNotProjectMember(Long projectId, List<GetProjectMemberResponse> projects) {
        return projects.stream()
                .noneMatch(getProjectMemberResponse -> getProjectMemberResponse.projectId().equals(projectId));
    }

    private Long getProjectId(String path) {
        Map<String, String> extracted = pathPattern.matchAndExtract(PathContainer.parsePath(path)).getUriVariables();
        Long projectId;
        try {
            projectId = Long.parseLong(extracted.get("projectId"));
        } catch (NumberFormatException e) {
            log.error("e: {}", e.getMessage());
            throw new AccessDeniedException("프로젝트의 멤버가 아닙니다!");
        }
        return projectId;
    }
}
