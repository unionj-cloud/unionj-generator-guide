package cloud.unionj.guide.api.controller;

import cloud.unionj.guide.proto.UserProto;
import cloud.unionj.guide.vo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author created by wubin
 * @version v0.0.1
 * description: cloud.unionj.guide.api.controller
 * date:2021/6/9
 */
public class UserController implements UserProto {
  @Override
  public ResponseEntity<byte[]> getApiUserAvatar(Long id) {
    return null;
  }

  @Override
  public ResultVO<UserDetailVO> getApiUserDetail(Long id) {
    return null;
  }

  @Override
  public ResultVO<String> postApiUserEdit(String name, Integer age, String sex, Long id, MultipartFile avatar) {
    return null;
  }

  @Override
  public ResultVO<PageResultVO<UserDetailVO>> postApiUserPage(UserPageReqVO body) {
    return null;
  }

  @Override
  public ResultVO<UserRegisterRespVO> postApiUserRegister(String username, String password) {
    return null;
  }
}
