package com.ss.song.fegin;

import com.ss.song.convert.UserConverter;
import com.ss.song.mapper.UserMapper;
import com.ss.song.model.User;
import com.ss.song.service.UserService;
import com.ss.song.vo.CtUser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@RestController
public class PracticeClient implements IPracticeClient {

    @Value("${name}")
    private String userName;

    private final UserService userService;

    private final UserMapper userMapper;

    public PracticeClient(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public CtUser selectByPrimaryKey(Integer applyId) {
        List<User> userList = userService.selectById(2);
        User user = userList.get(0);

        return UserConverter.INSTANCE.toVo(user);
    }

    @Override
    @Transactional
    public void updateByPrimaryKey(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("服务2修改66666666");
        user.setAddress("江苏省南京市秣陵街道");
        User user1 = userMapper.selectByPrimaryKey(1);

        System.out.println(user1.getName());

        userService.updateByExample(user);

        throw new RuntimeException("服务2异常。。。");
    }
}
