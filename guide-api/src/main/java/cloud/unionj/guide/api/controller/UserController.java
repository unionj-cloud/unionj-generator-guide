package cloud.unionj.guide.api.controller;

import cloud.unionj.guide.proto.UserProto;
import cloud.unionj.guide.vo.*;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author created by wubin
 * @version v0.0.1
 * description: cloud.unionj.guide.api.controller
 * date:2021/6/9
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserProto {

  private final MongoTemplate mongoTemplate;

  @SneakyThrows
  @Override
  public ResponseEntity<byte[]> getApiUserAvatar(String id) {
    UserDetailVO vo = mongoTemplate.findById(id, UserDetailVO.class, "guide");
    File file = new File(vo.getAvatar());
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
        .body(Files.readAllBytes(Paths.get(vo.getAvatar())));
  }

  @Override
  public ResultVO<UserDetailVO> getApiUserDetail(String id) {
    UserDetailVO vo = mongoTemplate.findById(id, UserDetailVO.class, "guide");
    ResultVO<UserDetailVO> ret = new ResultVO<>();
    ret.setCode(0);
    ret.setData(vo);
    return ret;
  }

  @SneakyThrows
  @Override
  public ResultVO<String> postApiUserEdit(String name, Integer age, String sex, String id, MultipartFile avatar) {
    byte[] bytes = avatar.getBytes();
    String outputDir = System.getProperty("user.dir") + "/output";
    new File(outputDir).mkdirs();
    Path path = Paths.get(outputDir + "/" + avatar.getOriginalFilename());
    Files.write(path, bytes);
    DBObject user = BasicDBObjectBuilder.start()
        .add("name", name)
        .add("age", age)
        .add("sex", sex)
        .add("avatar", path.toString())
        .add("_id", new ObjectId(id))
        .get();
    mongoTemplate.save(user, "guide");
    ResultVO<String> ret = new ResultVO<>();
    ret.setCode(0);
    ret.setData("OK");
    return ret;
  }

  @Override
  public ResultVO<PageResultVO<UserDetailVO>> postApiUserPage(UserPageReqVO body) {
    List<UserDetailVO> vos = mongoTemplate.findAll(UserDetailVO.class, "guide");
    PageResultVO<UserDetailVO> pageResultVO = new PageResultVO<>();
    pageResultVO.setPages(1);
    pageResultVO.setCurrent(1);
    pageResultVO.setSize(10);
    pageResultVO.setTotal(1l);
    pageResultVO.setItems(vos);
    ResultVO<PageResultVO<UserDetailVO>> ret = new ResultVO<>();
    ret.setCode(0);
    ret.setData(pageResultVO);
    return ret;
  }

  @Override
  public ResultVO<UserRegisterRespVO> postApiUserRegister(String username, String password) {
    DBObject user = BasicDBObjectBuilder.start()
        .add("username", username)
        .add("password", password)
        .get();
    DBObject savedUser = mongoTemplate.save(user, "guide");
    UserRegisterRespVO vo = new UserRegisterRespVO();
    vo.setId(((ObjectId) savedUser.get("_id")).toString());
    ResultVO<UserRegisterRespVO> ret = new ResultVO<>();
    ret.setCode(0);
    ret.setData(vo);
    return ret;
  }
}
