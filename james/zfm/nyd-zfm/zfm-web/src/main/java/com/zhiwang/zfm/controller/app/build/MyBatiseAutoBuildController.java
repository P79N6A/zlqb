package com.zhiwang.zfm.controller.app.build;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhiwang.zfm.common.bulid.CreateJava;
import com.zhiwang.zfm.common.request.mybatise.MybatiseCreatorRequest;
import com.zhiwang.zfm.common.response.CommonResponse;


@RestController
@RequestMapping("/api/mybatise")
@Api(description = "代码生成神器")
public class MyBatiseAutoBuildController {
	
	@Value("${project.name:#{null}}")
	public String projectAbbr;

	@RequestMapping(value = "/createXX", method = RequestMethod.POST)
	@ApiOperation(value = "生成DAO、SERVICE")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse login(@ModelAttribute MybatiseCreatorRequest mybatiseCreatorRequest,HttpServletRequest request) throws IOException {
		System.out.println(111);
		// 设置返回类型
		CommonResponse common = new CommonResponse();
		try {
			String modulePakPath = "\\src\\main";
			String modulePackage = "com.zhiwang." + projectAbbr;
			String database = "zfm_db";
			CreateJava.create(mybatiseCreatorRequest.getTableName(), mybatiseCreatorRequest.getCodeName(), modulePakPath,
					modulePackage, database, mybatiseCreatorRequest.getPrefix(), mybatiseCreatorRequest.getModelName());
			common.setMsg("111");
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			common.setSuccess(false);
		}
		return common;
	}
	
}
