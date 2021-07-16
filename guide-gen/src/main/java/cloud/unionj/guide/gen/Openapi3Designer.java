package cloud.unionj.guide.gen;

import cloud.unionj.generator.openapi3.PathConfig;
import cloud.unionj.generator.openapi3.expression.paths.ParameterBuilder;
import cloud.unionj.generator.openapi3.model.Openapi3;
import cloud.unionj.generator.openapi3.model.Schema;
import cloud.unionj.generator.openapi3.model.paths.Parameter;

import static cloud.unionj.generator.openapi3.PathHelper.get;
import static cloud.unionj.generator.openapi3.PathHelper.post;
import static cloud.unionj.generator.openapi3.dsl.Generic.generic;
import static cloud.unionj.generator.openapi3.dsl.Openapi3.openapi3;
import static cloud.unionj.generator.openapi3.dsl.Schema.schema;
import static cloud.unionj.generator.openapi3.dsl.SchemaHelper.*;
import static cloud.unionj.generator.openapi3.dsl.info.Info.info;
import static cloud.unionj.generator.openapi3.dsl.servers.Server.server;

public class Openapi3Designer {

  private static Schema ResultVO = schema(sb -> {
    sb.type("object");
    sb.title("ResultVO");
    sb.properties("code", int32);
    sb.properties("msg", string);
    sb.properties("data", T);
  });

  public static Schema UserRegisterFormVO = schema(sb -> {
    sb.type("object");
    sb.title("UserRegisterFormVO");
    sb.description("用户注册表单");
    sb.properties("username", string("用户名"));
    sb.properties("password", string("密码"));
  });

  public static Schema UserRegisterRespVO = schema(sb -> {
    sb.type("object");
    sb.title("UserRegisterRespVO");
    sb.description("用户注册结果");
    sb.properties("id", string("用户ID"));
  });

  public static Schema ResultVOUserRegisterRespVO = generic(gb -> {
    gb.generic(ResultVO, ref(UserRegisterRespVO.getTitle()));
  });

  public static Schema UserEditFormVO = schema(sb -> {
    sb.type("object");
    sb.title("UserEditFormVO");
    sb.description("用户信息编辑表单");
    sb.properties("name", string("真实姓名"));
    sb.properties("age", int32("年龄"));
    sb.properties("sex", enums("性别", new String[]{"BOY", "GIRL"}));
    sb.properties("avatar", file("用户头像"));
  });

  public static Schema ResultVOstring = generic(gb -> {
    gb.generic(ResultVO, string);
  });

  public static Schema UserDetailVO = schema(sb -> {
    sb.type("object");
    sb.title("UserDetailVO");
    sb.description("用户详情");
    sb.properties("id", string("用户ID"));
    sb.properties("name", string("真实姓名"));
    sb.properties("age", int32("年龄"));
    sb.properties("sex", enums("性别", new String[]{"BOY", "GIRL"}));
    sb.properties("avatar", string("用户头像下载地址"));
  });

  public static Schema ResultVOUserDetailVO = generic(gb -> {
    gb.generic(ResultVO, ref(UserDetailVO.getTitle()));
  });

  public static Schema UserPageReqVO = schema(sb -> {
    sb.type("object");
    sb.title("UserPageReqVO");
    sb.description("用户列表分页查询条件");
    sb.properties("size", int32("每页多少条数据"));
    sb.properties("current", int32("第几页"));
    sb.properties("sort", string("排序条件字符串：排序字段前使用'-'(降序)和'+'(升序)号表示排序规则，多个排序字段用','隔开",
        "+age,-create_at"));
    sb.properties("sex", string("筛选条件：用户性别"));
  });

  public static Schema PageResultVO = schema(sb -> {
    sb.type("object");
    sb.title("PageResultVO");
    sb.properties("items", ListT);
    sb.properties("total", int64("总数"));
    sb.properties("size", int32("每页多少条数据"));
    sb.properties("current", int32("当前页码"));
    sb.properties("pages", int32("总页数"));
  });

  public static Schema PageResultVOUserDetailVO = generic(gb -> {
    gb.generic(PageResultVO, ref(UserDetailVO.getTitle()));
  });


  public static Schema ResultVOPageResultVOUserDetailVO = generic(gb -> {
    gb.generic(ResultVO, ref(PageResultVOUserDetailVO.getTitle()));
  });

  public static Openapi3 design() {
    Openapi3 openAPI3 = openapi3(ob -> {
      info(ib -> {
        ib.title("用户管理模块");
        ib.version("v1.0.0");
      });

      server(sb -> {
        sb.url("http://unionj.cloud");
      });

      post("/api/user/register", PathConfig.builder()
          .summary("用户注册接口")
          .tags(new String[]{"用户管理模块", "User"})
          .reqSchema(UserRegisterFormVO)
          .reqSchemaType(PathConfig.SchemaType.FORMDATA)
          .respSchema(ResultVOUserRegisterRespVO)
          .build());

      post("/api/user/edit", PathConfig.builder()
          .summary("用户信息编辑接口")
          .tags(new String[]{"用户管理模块", "User"})
          .parameters(new Parameter[]{
              ParameterBuilder.builder().name("id").description("用户ID").in(Parameter.InEnum.QUERY).required(true).schema(string).build(),
          })
          .reqSchema(UserEditFormVO)
          .reqSchemaType(PathConfig.SchemaType.FORMDATA)
          .respSchema(ResultVOstring)
          .build());

      get("/api/user/detail", PathConfig.builder()
          .summary("用户信息查询接口")
          .tags(new String[]{"用户管理模块", "User"})
          .parameters(new Parameter[]{
              ParameterBuilder.builder().name("id").description("用户ID").in(Parameter.InEnum.QUERY).required(true).schema(string).build(),
          })
          .respSchema(ResultVOUserDetailVO)
          .build());

      get("/api/user/avatar", PathConfig.builder()
          .summary("用户头像下载接口")
          .tags(new String[]{"用户管理模块", "User"})
          .parameters(new Parameter[]{
              ParameterBuilder.builder().name("id").description("用户ID").in(Parameter.InEnum.QUERY).required(true).schema(string).build(),
          })
          .respSchema(file("用户头像"))
          .build());

      post("/api/user/page", PathConfig.builder()
          .summary("用户分页列表接口")
          .tags(new String[]{"用户管理模块", "User"})
          .reqSchema(UserPageReqVO)
          .respSchema(ResultVOPageResultVOUserDetailVO)
          .build());
    });
    return openAPI3;
  }

}
