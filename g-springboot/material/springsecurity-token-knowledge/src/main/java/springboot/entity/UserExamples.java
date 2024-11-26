package springboot.entity;

import java.util.List;

public enum UserExamples {
    //123456
    ROOT(new UserEntity(1, "root", "$2a$10$0cMLcvgvmq7tSa0.l4LuCeTSuXbTNhcl7oW6jwP7gt5uDg.wyakHy", List.of("admin"))),
    //654321
    USER1(new UserEntity(2, "Ross", "$2a$10$pjjNjL.ss0ednp1iT7fr8OazKug9QT5N8pPXzeRP6osVC6mx0KB/u", List.of("user:view"))),
    //114514
    USER2(new UserEntity(3, "Julie", "$2a$10$6hHtN.XaJ7Rwe1ibOVlfeu5g.49SSh/fA2rU65Ux28JRHKCJxUqFq", List.of("user:view", "user:add", "user:edit"))),
    //000000
    USER3(new UserEntity(4, "Gloria", "$2a$10$BzFxW4rpd/kSLbJohT9SLuGxluurOg/J8nl2hTTHy6HAgHNGFntK6", List.of()));
    private final UserEntity userEntity;

    UserExamples(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
