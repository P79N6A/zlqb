package ${modulePackage}.controller${moduleName};

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apt.core.model.PageView;
import com.apt.util.consts.BaseConsts;
import com.apt.core.controller.BaseController;

import ${modulePackage}.model.${className};
import ${modulePackage}.service.${className}Service;
import ${modulePackage}.controller.form.${className}SearchForm;

/**
 * ${codeName}控制器
 *
 */
@Controller
public class ${className}Controller extends BaseController {

	private static final Logger logger = Logger.getLogger(${className}Controller.class);
	
	/**
	 * ${codeName}服务类
	 */
	@Resource(name = "${lowerName}ServiceImpl")
	private ${className}Service<${className}> ${lowerName}Service;
	
	/**
	 * 列出${codeName}
	 */
	@RequestMapping("/list")
	public String list(${className}SearchForm form, ModelMap map) {
		try {
			//设置查询排序
			//TODO eg:form.getOrderby().put("id", "asc");
			
			//创建页面对象
			PageView<${className}> pageView = new PageView<${className}>(BaseConsts.CURRENTPAGE, form.getPage());
			form.setPage(pageView.getFirstResult());
			form.setRows(pageView.getMaxresult());
			pageView.setQueryResult(${lowerName}Service.getData(form));
			map.addAttribute("pageView", pageView);
			map.addAttribute("form", form);
			//TODO 转发页面
			return "";
		} catch(Exception e){
			logger.error(e);
			return "error";
		}
	}
}
