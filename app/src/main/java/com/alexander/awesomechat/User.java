package com.alexander.awesomechat;

public class User {

    private String email;
    private String name;
    private String id;
    private String profilePhoto;
//    private int avatarMockUpResource;

    public User() {
    }

    public User(String email, String name, String id, String profilePhoto) {
        this.email = email;
        this.name = name;
        this.id = id;
//        this.avatarMockUpResource = avatarMockUpResource;
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public int getAvatarMockUpResource() {
//        return avatarMockUpResource;
//    }
//
//    public void setAvatarMockUpResource(int avatarMockUpResource) {
//        this.avatarMockUpResource = avatarMockUpResource;
//    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
