package com.pyrenty.akl.web.rest.dto;

/**
 * DTO for Team Request managenet (accepting and managing team members)
 */
public class TeamRequestDTO {
    private Long member;
    private String role;
    private Long swapMember;

    public TeamRequestDTO() {

    }

    public Long getMember() {
        return member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getSwapMember() {
        return swapMember;
    }

    public void setSwapMember(Long swapMember) {
        this.swapMember = swapMember;
    }
}
